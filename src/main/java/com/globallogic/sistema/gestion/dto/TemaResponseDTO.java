package com.globallogic.sistema.gestion.dto;

import com.globallogic.sistema.gestion.model.Tema;

public record TemaResponseDTO(
        Long id,
        String titulo,
        String descripcion,
        Double duracionHoras
) {
    public static TemaResponseDTO fromTema(Tema tema){
        return new TemaResponseDTO(tema.getId(), tema.getTitulo(), tema.getDescripcion(), tema.getDuracion());
    }
}
