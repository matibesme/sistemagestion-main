package com.globallogic.sistema.gestion.configuration;

import com.globallogic.sistema.gestion.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AdminSeederConfig {
    private final UserService userService;

    @Bean
    @Transactional
    public CommandLineRunner initDefaultAdmin(){
        return args -> {
            userService.createDefaultAdmins();
        };
    }
}
