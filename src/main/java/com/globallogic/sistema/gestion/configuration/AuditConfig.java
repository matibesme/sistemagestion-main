package com.globallogic.sistema.gestion.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "clockDateTimeProvider")
public class AuditConfig {

    @Bean
    public DateTimeProvider clockDateTimeProvider(Clock clock) {
        return () -> Optional.of(Instant.now(clock));
    }
}