package com.globallogic.sistema.gestion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequestDTO(
        @NotNull @NotBlank String token,
        @NotNull @NotBlank @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") String newPassword
) {}