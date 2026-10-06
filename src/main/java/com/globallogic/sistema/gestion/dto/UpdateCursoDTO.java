package com.globallogic.sistema.gestion.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateCursoDTO(
        @NotNull @NotBlank String nombre,
        @NotNull @NotBlank String descripcion,
        @NotNull List<@Valid ExamenRequestDTO> evaluaciones,
        @NotNull CriterioAprobacionDTO criterios
) {
}
