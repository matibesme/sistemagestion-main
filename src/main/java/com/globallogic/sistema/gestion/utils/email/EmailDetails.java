package com.globallogic.sistema.gestion.utils.email;

import lombok.Builder;

@Builder
public record EmailDetails(
        String to,
        String subject,
        String body
) {
}
