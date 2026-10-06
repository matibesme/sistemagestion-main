package com.globallogic.sistema.gestion.controller;

import com.globallogic.sistema.gestion.dto.CursosAlumnoResponseDTO;
import com.globallogic.sistema.gestion.service.AlumnoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alumnos")
public class AlumnosController {

    private AlumnoService alumnoService;

    @GetMapping("/{id}/cursos")
    public ResponseEntity<CursosAlumnoResponseDTO> obtenerCursosDeUnAlumno(
            @PathVariable Long alumnoId
    ) {
        CursosAlumnoResponseDTO response = this.alumnoService.obtenerCursosDeUnAlumno(alumnoId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
