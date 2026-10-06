package com.globallogic.sistema.gestion.repository;

import com.globallogic.sistema.gestion.model.Alumno;
import com.globallogic.sistema.gestion.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Long> {
    boolean existsAlumnoByUser(User user);
    Alumno getAlumnoByUser(User user);
    Optional<Alumno> findByUserId(Long id);
}
