package com.globallogic.sistema.gestion.service;

import com.globallogic.sistema.gestion.exception.NotFoundException;
import com.globallogic.sistema.gestion.model.Profesor;
import com.globallogic.sistema.gestion.model.User;
import com.globallogic.sistema.gestion.repository.ProfesorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfesorService {

    private final ProfesorRepository repository;
    private final Clock clock;

    public Profesor getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Profesor no encontrado -- id: " + id));
    }

    @Transactional
    public void crearPerfilProfesor(User user) {
        if (!repository.existsProfesorByUser(user)) {
            log.info("Creando perfil de profesor para usuario ID: {}", user.getId());
            repository.save(new Profesor(user, LocalDateTime.now(clock)));
        }
    }

    @Transactional
    public void eliminarPerfilProfesorPorUserId(Long userId) {
        repository.findByUserId(userId).ifPresentOrElse(
                profesor -> {
                    repository.delete(profesor);
                    log.info("Profesor asociado al usuario ID {} eliminado exitosamente.", userId);
                },
                () -> log.debug("El usuario ID {} no tenía perfil de Profesor. Nada que borrar.", userId)
        );
    }
}