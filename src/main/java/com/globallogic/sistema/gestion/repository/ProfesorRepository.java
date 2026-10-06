package com.globallogic.sistema.gestion.repository;

import com.globallogic.sistema.gestion.model.Profesor;
import com.globallogic.sistema.gestion.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
    boolean existsProfesorByUser(User user);

    Profesor getProfesorByUser(User user);

    Optional<Profesor> findByUserId(Long id);
}
