package com.globallogic.sistema.gestion.dto;

public record LoginResult(
        AuthResponseDTO loginResponse,
        String refreshToken
) {
}
