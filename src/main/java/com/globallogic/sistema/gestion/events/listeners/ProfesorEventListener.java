package com.globallogic.sistema.gestion.events.listeners;

import com.globallogic.sistema.gestion.events.UserDeletedEvent;
import com.globallogic.sistema.gestion.events.UserRoleAddedEvent;
import com.globallogic.sistema.gestion.service.ProfesorService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProfesorEventListener {

    private final ProfesorService profesorService;

    @EventListener(condition = "#event.role.name() == 'PROFESOR'")
    public void handleProfesorCreated(UserRoleAddedEvent event) {
        profesorService.crearPerfilProfesor(event.user());
    }

    @EventListener
    public void handleUserDeleted(UserDeletedEvent event) {
        profesorService.eliminarPerfilProfesorPorUserId(event.userId());
    }
}