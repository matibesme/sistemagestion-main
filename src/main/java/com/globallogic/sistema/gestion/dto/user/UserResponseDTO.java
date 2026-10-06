package com.globallogic.sistema.gestion.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.globallogic.sistema.gestion.model.Roles;
import com.globallogic.sistema.gestion.model.User;

import java.util.ArrayList;
import java.util.List;

public record UserResponseDTO(
        Long id,
        @JsonProperty("nombre")
        String name,
        String email,
        List<Roles> roles,
        String estado
) {
    public static UserResponseDTO fromUser(User user){
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                new ArrayList<>(user.getRoles()),
                user.getEstado().toString()
        );
    }
}
