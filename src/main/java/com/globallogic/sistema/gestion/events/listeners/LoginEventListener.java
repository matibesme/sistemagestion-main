package com.globallogic.sistema.gestion.events.listeners;
import com.globallogic.sistema.gestion.events.LoginFailedEvent;
import com.globallogic.sistema.gestion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginEventListener {
    private static final int MAX_INTENTOS_LOGIN = 3;
    private static final int MINUTOS_BLOQUEO = 1;

    private final UserRepository userRepository;
    private final Clock clock;

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleLoginFailed(LoginFailedEvent event) {
        log.info("Procesando intento fallido para el usuario: {}", event.email());

        userRepository.findByEmail(event.email()).ifPresent(user -> {
            user.registrarIntentoFallido(MAX_INTENTOS_LOGIN, Duration.ofMinutes(MINUTOS_BLOQUEO) , LocalDateTime.now(clock));
            userRepository.saveAndFlush(user);
        });
    }
}
