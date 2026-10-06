package com.globallogic.sistema.gestion.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.globallogic.sistema.gestion.model.Roles;
import jakarta.validation.constraints.*;

import java.util.List;

public record UserCreateDTO(
        @NotNull @NotBlank @JsonProperty("nombre") String username,
        @NotNull @NotBlank @Email String email,
        @NotNull @NotBlank @Size(min = 8) @JsonProperty("clave") String password,
        @NotNull @NotEmpty List<Roles> roles
) {}