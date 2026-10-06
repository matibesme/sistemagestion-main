package com.globallogic.sistema.gestion.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RecoverPasswordResponseDTO(
       @JsonProperty("mensaje") String message
) {
}
