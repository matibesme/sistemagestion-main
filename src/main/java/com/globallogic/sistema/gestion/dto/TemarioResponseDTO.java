package com.globallogic.sistema.gestion.dto;

import java.util.List;

public record TemarioResponseDTO(
        String nombreCurso,
        List<TemaTemarioResponseDTO> temas
) {
}
