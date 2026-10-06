package com.globallogic.sistema.gestion.controller;

import com.globallogic.sistema.gestion.dto.InscripcionRequestDTO;
import com.globallogic.sistema.gestion.dto.InscripcionResponseDTO;
import com.globallogic.sistema.gestion.service.InscripcionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cursos")
public class InscripcionController {
    private InscripcionService inscripcionService;

    @PostMapping("/{id}/inscripciones")
    public ResponseEntity<InscripcionResponseDTO> handleInscripcion(
            @PathVariable Long id,
            @RequestBody InscripcionRequestDTO data
            ) {
        InscripcionResponseDTO inscripcion = this.inscripcionService.procesarInscripcion(id, data);
        return ResponseEntity.status(HttpStatus.CREATED).body(inscripcion);
    }

}
