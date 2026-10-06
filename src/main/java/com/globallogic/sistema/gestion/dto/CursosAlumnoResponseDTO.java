package com.globallogic.sistema.gestion.dto;

import java.util.List;

public record CursosAlumnoResponseDTO(
        String nombreAlumno,
        List<String> cursos
) {

}
