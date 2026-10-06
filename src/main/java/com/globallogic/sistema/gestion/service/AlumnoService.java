package com.globallogic.sistema.gestion.service;


import com.globallogic.sistema.gestion.dto.CursosAlumnoResponseDTO;
import com.globallogic.sistema.gestion.exception.NotFoundException;
import com.globallogic.sistema.gestion.model.Alumno;
import com.globallogic.sistema.gestion.model.Inscripcion;
import com.globallogic.sistema.gestion.model.User;
import com.globallogic.sistema.gestion.repository.AlumnoRepository;
import com.globallogic.sistema.gestion.repository.InscripcionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;
    private final InscripcionRepository inscripcionRepository;

    @Transactional
    public void crearPerfilAlumno(User user) {
        if (!alumnoRepository.existsAlumnoByUser(user)) {
            log.info("Creando perfil de alumno para usuario ID: {}", user.getId());
            Alumno alumno = Alumno.builder()
                    .nombre(user.getName())
                    .email(user.getEmail())
                    .user(user)
                    .build();
            alumnoRepository.save(alumno);
        }
    }

    @Transactional
    public void eliminarPerfilAlumnoPorUserId(Long userId) {
        alumnoRepository.findByUserId(userId).ifPresentOrElse(
                alumno -> {
                    alumnoRepository.delete(alumno);
                    log.info("Alumno asociado al usuario ID {} eliminado exitosamente.", userId);
                },
                () -> log.debug("El usuario ID {} no tenía perfil de Alumno. Nada que borrar.", userId)
        );
    }

    public CursosAlumnoResponseDTO  obtenerCursosDeUnAlumno(Long alumnoId) {

        Alumno alumno = this.alumnoRepository.findById(alumnoId).orElseThrow(() -> new NotFoundException("El alumno no existe"));

        List<Inscripcion> inscripcionList = this.inscripcionRepository.findByAlumno_Id(alumnoId);

        List<String> cursosNombres = inscripcionList.stream()
                .map(inscripcion -> inscripcion.getCurso().getNombre())
                .toList();

        return new CursosAlumnoResponseDTO(
                alumno.getNombre(),
                cursosNombres
        );
    }

}