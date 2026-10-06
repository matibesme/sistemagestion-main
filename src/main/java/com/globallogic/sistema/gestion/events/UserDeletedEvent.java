package com.globallogic.sistema.gestion.events;

import lombok.NonNull;

public record UserDeletedEvent(@NonNull Long userId) {
}
