package com.globallogic.sistema.gestion.service;

import com.globallogic.sistema.gestion.dto.InscripcionRequestDTO;
import com.globallogic.sistema.gestion.dto.InscripcionResponseDTO;
import com.globallogic.sistema.gestion.exception.NotFoundException;
import com.globallogic.sistema.gestion.exception.YaInscriptoException;
import com.globallogic.sistema.gestion.model.Alumno;
import com.globallogic.sistema.gestion.model.Curso;
import com.globallogic.sistema.gestion.model.Inscripcion;
import com.globallogic.sistema.gestion.repository.AlumnoRepository;
import com.globallogic.sistema.gestion.repository.CursoRepository;
import com.globallogic.sistema.gestion.repository.InscripcionRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class InscripcionService {

    private final AlumnoRepository alumnoRepository;
    private final InscripcionRepository inscripcionRepository;
    private final CursoRepository cursoRepository;
    private final Clock clock;

    @PreAuthorize("hasRole('ALUMNO')")
    public InscripcionResponseDTO procesarInscripcion(Long id, InscripcionRequestDTO data){

        Alumno alumno = alumnoRepository.findById(data.alumnoId()).orElseThrow(() -> new NotFoundException("Alumno no encontrado"));


        if (inscripcionRepository.existsByAlumnoIdAndCursoId(alumno.getId(), id)) {
            throw new YaInscriptoException("El alumno ya esta inscripto en este curso.");
        }

        Curso curso = cursoRepository.findById(id).orElseThrow(() -> new NotFoundException("Curso no encontrado"));

        Inscripcion nuevaInscripcion = new Inscripcion();
        nuevaInscripcion.setCreatedAt(LocalDateTime.now(clock));
        nuevaInscripcion.setAlumno(alumno);
        nuevaInscripcion.setCurso(curso);

        Inscripcion inscripcionGuarada = inscripcionRepository.save(nuevaInscripcion);

        return new InscripcionResponseDTO(
               "Inscripcion registrada con exito",
                inscripcionGuarada.getCurso().getNombre(),
                inscripcionGuarada.getAlumno().getNombre()
        );
    }


}
