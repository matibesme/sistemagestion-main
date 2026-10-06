package com.globallogic.sistema.gestion.dto;

import com.globallogic.sistema.gestion.model.Tema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TemaAddDTO(
        @NotNull @NotBlank String titulo,
        @NotNull @NotBlank String descripcion,
        @NotNull @Positive Double duracionHoras
) {
    public static Tema fromDto(TemaAddDTO dto){
        return new Tema(dto.titulo(), dto.descripcion(), dto.duracionHoras());
    }
}
