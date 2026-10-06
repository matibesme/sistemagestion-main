package com.globallogic.sistema.gestion.dto;

import jakarta.validation.constraints.Email;

public record UpdateUserDTO(
        String nombre,
        @Email
        String email
) {
}
