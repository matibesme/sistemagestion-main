package com.globallogic.sistema.gestion.controller;


import com.globallogic.sistema.gestion.dto.*;
import com.globallogic.sistema.gestion.dto.user.UserLoginDTO;
import com.globallogic.sistema.gestion.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.Duration;

/**
 * Controller for authentication-related endpoints.
 * objeciones: no estoy de acuerdo con devolver 404 ni en login ni en recuperar contrasenia
 * esto supone una vulnerabilidad de seguridad en donde damos informacion de que emails estan registrados y cuales no
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    public static final String REFRESH_COOKIE_NAME = "refreshToken";

    /**
     * Endpoint to register a new user.
     */
//    @PostMapping("/register")
//    public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserCreateDTO user) {
//        return ResponseEntity.ok(authService.registerUser(user));
//    }

    /**
     * Endpoint to login and receive a JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid UserLoginDTO loginRequest) {

        LoginResult result = authService.login(loginRequest);

        ResponseCookie refreshCookie = createRefreshCookie(result.refreshToken(), Duration.ofDays(7));

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(result.loginResponse());
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDTO> refreshToken(@CookieValue(name = REFRESH_COOKIE_NAME, required = true) String refreshToken) {
        var result = authService.refreshToken(refreshToken);

        ResponseCookie refreshCookie = createRefreshCookie(result.refreshToken(), Duration.ofDays(7));
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(result.loginResponse());

    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = REFRESH_COOKIE_NAME, required = false) String refreshToken) {
        authService.logout(refreshToken);

        ResponseCookie deleteCookie = createRefreshCookie("", Duration.ZERO);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();

    }

    @PostMapping("/recuperar")
    public ResponseEntity<RecoverPasswordResponseDTO> recoverPassword(@RequestBody @Valid RecoverPasswordRequestDTO request) {
        return ResponseEntity.ok(authService.recoverPassword(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequestDTO request) {
        authService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    private ResponseCookie createRefreshCookie(String token, Duration maxAge) {

        return ResponseCookie.from(REFRESH_COOKIE_NAME, token)
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .maxAge(maxAge)
                .build();
    }
}
