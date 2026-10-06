package com.globallogic.sistema.gestion.service;

import com.globallogic.sistema.gestion.exception.NotFoundException;
import com.globallogic.sistema.gestion.model.RefreshToken;
import com.globallogic.sistema.gestion.model.User;
import com.globallogic.sistema.gestion.repository.RefreshTokenRepository;
import com.globallogic.sistema.gestion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final Clock clock;

    @Value("${jwt.refresh-validity-days}")
    private long refreshTokenValidityDays;

    public RefreshToken findByToken(String token){
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new NotFoundException("Token not found"));
    }

    @Transactional
    public RefreshToken createRefreshToken(Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        RefreshToken token = RefreshToken.of(UUID.randomUUID().toString(), LocalDateTime.now(clock).plusDays(refreshTokenValidityDays), user);
        return refreshTokenRepository.save(token);

    }

    @Transactional
    public void revokeToken(String token){
        refreshTokenRepository.findByToken(token)
                .ifPresent(RefreshToken::revoke);
    }
}
