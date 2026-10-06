package com.globallogic.sistema.gestion.events.listeners;


import com.globallogic.sistema.gestion.events.UserDeletedEvent;
import com.globallogic.sistema.gestion.events.UserRoleAddedEvent;
import com.globallogic.sistema.gestion.service.AlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlumnoEventListener {

    private final AlumnoService alumnoService;

    @EventListener(condition = "#event.role.name() == 'ALUMNO'")
    public void handleAlumnoCreated(UserRoleAddedEvent event) {
        alumnoService.crearPerfilAlumno(event.user());
    }

    @EventListener
    public void handleUserDeleted(UserDeletedEvent event) {
        alumnoService.eliminarPerfilAlumnoPorUserId(event.userId());
    }
}