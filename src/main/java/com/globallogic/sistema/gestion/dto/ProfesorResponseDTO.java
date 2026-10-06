package com.globallogic.sistema.gestion.dto;

import com.globallogic.sistema.gestion.model.Profesor;

public record ProfesorResponseDTO(
        Long id,
        String nombre
) {
    public static ProfesorResponseDTO fromProfesor(Profesor profesor){
        return new ProfesorResponseDTO(profesor.getId(), profesor.getNombre());
    }
}
