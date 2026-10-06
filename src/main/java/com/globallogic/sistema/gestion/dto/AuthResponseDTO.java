package com.globallogic.sistema.gestion.dto;

import com.globallogic.sistema.gestion.dto.user.UserResponseDTO;
import lombok.Builder;

@Builder
public record AuthResponseDTO(
        UserResponseDTO user,
        String accessToken
) {
}
