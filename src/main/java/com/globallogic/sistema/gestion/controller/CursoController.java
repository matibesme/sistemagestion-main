package com.globallogic.sistema.gestion.controller;

import com.globallogic.sistema.gestion.dto.*;
import com.globallogic.sistema.gestion.dto.curso.CursoCreateDTO;
import com.globallogic.sistema.gestion.dto.curso.CursoResponseDTO;
import com.globallogic.sistema.gestion.model.Curso;
import com.globallogic.sistema.gestion.service.CursoService;
import com.globallogic.sistema.gestion.service.InscripcionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cursos")
public class CursoController {
    private CursoService cursoService;
    private InscripcionService inscripcionService;

    public CursoController(CursoService cursoService){
        this.cursoService = cursoService;
    }

    @PostMapping
    public ResponseEntity<CursoResponseDTO> createCurso(@RequestBody @Valid  CursoCreateDTO data){
        CursoResponseDTO curso = this.cursoService.createCurso(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(curso);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoResponseDTO> updateCurso(
            @RequestBody @Valid UpdateCursoDTO data,
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails){
        CursoResponseDTO cursoModificado = this.cursoService.updateCurso(data, id, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(cursoModificado);
    }

    @PostMapping("/{id}/temas")
    public ResponseEntity<TemaResponseDTO> addTema(
            @RequestBody @Valid TemaAddDTO data,
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        TemaResponseDTO temaAgregado = this.cursoService.agregarTema(id, data, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(temaAgregado);

    }

    @DeleteMapping("/{curso_id}/temas/{tema_id}")
    public ResponseEntity<Void> deleteTema(
            @PathVariable Long curso_id,
            @PathVariable Long tema_id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        this.cursoService.deleteTema(curso_id, tema_id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/temario")
    public ResponseEntity<TemarioResponseDTO> obtenerTemario (
        @PathVariable Long id
    ) {
        TemarioResponseDTO temario = this.cursoService.obtenerTemario(id);
        return ResponseEntity.status(HttpStatus.OK).body(temario);
    }
}
