package com.globallogic.sistema.gestion.dto;

import com.globallogic.sistema.gestion.model.Roles;
import jakarta.validation.constraints.NotNull;

public record AddRoleDTO(
        @NotNull Roles rol
) {
}
