package com.globallogic.sistema.gestion.dto.curso;

import com.globallogic.sistema.gestion.dto.ProfesorResponseDTO;
import com.globallogic.sistema.gestion.model.Curso;
import lombok.Builder;
import lombok.NonNull;


import java.util.List;

@Builder
public record CursoResponseDTO(
        Long id,
        String nombre,
        String descripcion,
        List<ProfesorResponseDTO> profesores,
        Integer temas,
        Double duracionTotalHoras
) {
    public static CursoResponseDTO fromCurso(@NonNull Curso curso){
        return CursoResponseDTO.builder()
                .id(curso.getId())
                .nombre(curso.getNombre())
                .descripcion(curso.getDescripcion())
                .temas(curso.cantTemas())
                .profesores(curso.getProfesoresACargo().stream().map(ProfesorResponseDTO::fromProfesor).toList())
                .duracionTotalHoras(curso.duracion())
                .build();
    }
}
