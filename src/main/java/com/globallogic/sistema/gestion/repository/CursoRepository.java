package com.globallogic.sistema.gestion.repository;

import com.globallogic.sistema.gestion.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

    boolean existsByIdAndProfesoresACargo_User_EmailIgnoreCase(Long idCurso, String email);
}
