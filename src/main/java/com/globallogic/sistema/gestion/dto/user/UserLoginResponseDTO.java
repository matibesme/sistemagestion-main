package com.globallogic.sistema.gestion.dto.user;

import java.util.List;

public record UserLoginResponseDTO(
        String mensaje,
        UsuarioDTO usuario,
        String token
) {

    public record UsuarioDTO(
            Long id,
            String nombre,
            List<String> roles
    ) {}
}
