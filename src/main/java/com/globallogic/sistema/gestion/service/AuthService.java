package com.globallogic.sistema.gestion.service;

import com.globallogic.sistema.gestion.dto.*;
import com.globallogic.sistema.gestion.dto.user.UserCreateDTO;
import com.globallogic.sistema.gestion.dto.user.UserLoginDTO;
import com.globallogic.sistema.gestion.dto.user.UserResponseDTO;
import com.globallogic.sistema.gestion.events.LoginFailedEvent;
import com.globallogic.sistema.gestion.exception.EmailAlreadyExistsException;
import com.globallogic.sistema.gestion.exception.LockedAccountException;
import com.globallogic.sistema.gestion.exception.NotFoundException;
import com.globallogic.sistema.gestion.model.PasswordResetToken;
import com.globallogic.sistema.gestion.model.RefreshToken;
import com.globallogic.sistema.gestion.model.User;
import com.globallogic.sistema.gestion.repository.PasswordResetTokenRepository;
import com.globallogic.sistema.gestion.repository.UserRepository;
import com.globallogic.sistema.gestion.utils.email.EmailDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing users and handling authentication.
 * Implements UserDetailsService to integrate with Spring Security.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final EmailService emailService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetTokenRepository tokenRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    @Value("${frontend.reset-password-url}")
    private String frontendResetUrl;
    /**
     * Registers a new user.
     * Encrypts the user's password before saving it to the database.
     * Sets a default role if none is provided.
     */
    public User registerUser(UserCreateDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.email())) {
            throw new EmailAlreadyExistsException(userDTO.email());
        }

        User user = new User();
        user.setPassword(passwordEncoder.encode(userDTO.password()));
        user.setName(userDTO.username());
        user.setEmail(userDTO.email());

        userDTO.roles().forEach(user::addRol);
        return userRepository.save(user);
    }

    @Transactional
    public LoginResult login(UserLoginDTO loginRequest){

        var user = userRepository.findByEmail(loginRequest.email()).orElseThrow(() -> {
            log.info("Mail no encontrado en login");
            return new BadCredentialsException("Email o contraseña incorrectos");
        });

        if(user.estaBloqueado(LocalDateTime.now(clock))){
            userRepository.saveAndFlush(user);
            throw new LockedAccountException("Su cuenta ha sido bloqueada por múltiples intentos fallidos. Intente más tarde o contacte al administrador.", user.tiempoRestanteBloqueado(LocalDateTime.now(clock)));
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );

            user.registarLoginExitoso();
            userRepository.save(user);

        } catch (BadCredentialsException e) {
            eventPublisher.publishEvent(new LoginFailedEvent(loginRequest.email()));
            throw new BadCredentialsException("Email o contraseña incorrectos");
        }

        var userDetails = userDetailsService.loadUserByUsername(loginRequest.email());
        String accessToken = jwtService.generateAccessToken(userDetails);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        var authResponse =  AuthResponseDTO.builder()
                .user(UserResponseDTO.fromUser(user))
                .accessToken(accessToken)
                .build();

        return new LoginResult(
                authResponse,
                refreshToken.getToken()
        );
    }

    @Transactional
    public LoginResult refreshToken(String refreshToken){
        RefreshToken tokenDb = null;
        try {
            tokenDb = refreshTokenService.findByToken(refreshToken);
        } catch (NotFoundException e) {
            throw new IllegalArgumentException("Invalid refresh Token");
        }

        if(!tokenDb.isValid(LocalDateTime.now(clock))) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        tokenDb.revoke();

        var userDetails = userDetailsService.loadUserByUsername(tokenDb.getUser().getEmail());

        String newAccessToken = jwtService.generateAccessToken(userDetails);
        var newRefreshToken = refreshTokenService.createRefreshToken(tokenDb.getUser().getId());
        var authResponse =  AuthResponseDTO.builder()
                .user(UserResponseDTO.fromUser(newRefreshToken.getUser()))
                .accessToken(newAccessToken)
                .build();

        return new LoginResult(
                authResponse,
                newRefreshToken.getToken()
        );
    }

    @Transactional
    public void logout(String refreshToken){
        refreshTokenService.revokeToken(refreshToken);
    }


    @Transactional
    public RecoverPasswordResponseDTO recoverPassword(RecoverPasswordRequestDTO request) {
        var response = new RecoverPasswordResponseDTO("Si el correo electrónico existe en nuestro sistema, recibirá un enlace para restablecer su contraseña.");

        Optional<User> userOpt = userRepository.findByEmail(request.email());
        if (userOpt.isEmpty()) {
            return response;
        }

        User user = userOpt.get();

        String tokenValue = UUID.randomUUID().toString();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(tokenValue)
                .user(user)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();

        tokenRepository.save(resetToken);

        String recoveryLink = buildRecoveryLink(tokenValue);

        emailService.sendEmail(buildRecoverPasswordEmail(user, recoveryLink));

        return response;
    }

    private @NonNull String buildRecoveryLink(String tokenValue) {
        return frontendResetUrl + "?token=" + tokenValue;
    }

    @Transactional
    public void resetPassword(ResetPasswordRequestDTO request) {
        PasswordResetToken resetToken = tokenRepository.findByToken(request.token())
                .orElseThrow(() -> new IllegalArgumentException("El token proporcionado no es válido o no existe."));

        if (!resetToken.isValid(LocalDateTime.now(clock))) {
            throw new IllegalArgumentException("El token ha expirado o ya ha sido utilizado.");
        }
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.newPassword()));

        user.registarLoginExitoso();
        userRepository.save(user);

        resetToken.consume();
        tokenRepository.save(resetToken);
    }

    private EmailDetails buildRecoverPasswordEmail(User user, String recoveryLink){

        String emailBody = String.format(
                "Hola %s,\n\nHaz recibido esta solicitud para restablecer la contraseña de tu cuenta.\n" +
                        "Por favor, haz clic en el siguiente enlace privado para configurar tu nueva contraseña:\n\n" +
                        "%s\n\n" +
                        "Este enlace expirará automáticamente en 15 minutos.\n" +
                        "Si tú no solicitaste esto, puedes ignorar este correo de forma segura.",
                user.getName(), recoveryLink
        );
        // TODO: pasar a HTML??
        return EmailDetails.builder()
                .to(user.getEmail())
                .subject("Restablecer Contraseña")
                .body(emailBody)
                .build();
    }

}
