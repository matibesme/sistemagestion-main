package com.globallogic.sistema.gestion.repository;

import com.globallogic.sistema.gestion.model.Alumno;
import com.globallogic.sistema.gestion.model.Curso;
import com.globallogic.sistema.gestion.model.Inscripcion;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    List<Inscripcion> getInscripcionsByAlumno(Alumno alumno);
    boolean existsByAlumnoAndCurso(Alumno alumno, Curso curso);
    boolean existsByAlumnoIdAndCursoId(Long alumnoId, Long cursoId);
    List<Inscripcion> findByAlumno_Id(Long alumnoId);
}
