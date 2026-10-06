package com.globallogic.sistema.gestion.events;

import com.globallogic.sistema.gestion.model.Roles;
import com.globallogic.sistema.gestion.model.User;
import lombok.Builder;

@Builder
public record UserRoleAddedEvent(
        User user,
        Roles role
) {
}
