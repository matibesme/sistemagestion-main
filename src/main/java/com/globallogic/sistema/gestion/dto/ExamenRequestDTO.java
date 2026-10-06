package com.globallogic.sistema.gestion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExamenRequestDTO(
        @NotBlank String nombre,
        @NotNull Long curso_id
) {
}
