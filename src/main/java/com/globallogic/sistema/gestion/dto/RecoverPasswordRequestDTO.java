package com.globallogic.sistema.gestion.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecoverPasswordRequestDTO(
        @NotNull @NotBlank @Email String email
) {
}
