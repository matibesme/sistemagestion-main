package com.globallogic.sistema.gestion.dto.curso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CursoCreateDTO(
        @NotNull @NotBlank
        String nombre,
        String descripcion,
        @NotEmpty @NotNull
        List<Long> profesoresIds) {
}
