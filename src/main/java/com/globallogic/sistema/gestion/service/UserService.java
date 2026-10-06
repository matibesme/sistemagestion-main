package com.globallogic.sistema.gestion.service;

import com.globallogic.sistema.gestion.dto.PagedResponseDTO;
import com.globallogic.sistema.gestion.dto.UpdateUserDTO;
import com.globallogic.sistema.gestion.dto.user.UserCreateDTO;
import com.globallogic.sistema.gestion.dto.user.UserResponseDTO;
import com.globallogic.sistema.gestion.events.UserDeletedEvent;
import com.globallogic.sistema.gestion.events.UserRoleAddedEvent;
import com.globallogic.sistema.gestion.exception.AlreadyHasRoleException;
import com.globallogic.sistema.gestion.exception.ConflictException;
import com.globallogic.sistema.gestion.exception.NotFoundException;
import com.globallogic.sistema.gestion.model.Roles;
import com.globallogic.sistema.gestion.model.User;
import com.globallogic.sistema.gestion.repository.AlumnoRepository;
import com.globallogic.sistema.gestion.repository.InscripcionRepository;
import com.globallogic.sistema.gestion.repository.ProfesorRepository;
import com.globallogic.sistema.gestion.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    @Value("${admin.default.email}")
    private String adminEmail;

    @Value("${admin.default.username}")
    private String adminUserName;

    @Value("${admin.default.password}")
    private String adminPassword;

    private final UserRepository userRepository;
    private final ProfesorRepository profesorRepository;
    private final AlumnoRepository alumnoRepository;
    private final InscripcionRepository inscripcionRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final ApplicationEventPublisher eventPublisher;

    public PagedResponseDTO<UserResponseDTO> getUsersByRole(Roles rol, Pageable pageable) {
        Page<User> users;
        if (rol == null) {
            users = userRepository.findAll(pageable);
        } else {
            users = userRepository.findByRolesContaining(rol, pageable);
        }

        return PagedResponseDTO.from(users.map(UserResponseDTO::fromUser));
    }

    public UserResponseDTO getUserResponseById(Long id) {
        User user = this.getUserById(id);
        return UserResponseDTO.fromUser(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        boolean puedeSerBorrado = true;
        if (this.alumnoRepository.existsAlumnoByUser(user)) {
            var alumno = this.alumnoRepository.getAlumnoByUser(user);
            var inscripciones = this.inscripcionRepository.getInscripcionsByAlumno(alumno);
            puedeSerBorrado = inscripciones.isEmpty();
        }
        if (this.profesorRepository.existsProfesorByUser(user)) {
            var profesor = this.profesorRepository.getProfesorByUser(user);
            puedeSerBorrado = profesor.getCantCursos() == 0;
        }
        if (!puedeSerBorrado) throw new ConflictException("El usuario aun tiene cursos");
        eventPublisher.publishEvent(new UserDeletedEvent(user.getId()));
        this.userRepository.deleteById(id);
    }

    public UserResponseDTO updateUser(UpdateUserDTO data, Long id) {
        User userToUpdate = this.getUserById(id);
        if (data != null && data.email() != null && !data.email().isEmpty()) userToUpdate.setEmail(data.email());
        if (data != null && data.email() != null && !data.nombre().isEmpty()) userToUpdate.setName(data.nombre());
        return UserResponseDTO.fromUser(this.userRepository.save(userToUpdate));
    }

    @Transactional
    public UserResponseDTO addRole(Long id, Roles rol) {

        User user = getUserById(id);

        if (user.tieneRol(rol)) {
            throw new AlreadyHasRoleException("Usuario: " + id + " ya tiene el rol " + rol.toString());
        }

        user.addRol(rol);
        userRepository.save(user);
        eventPublisher.publishEvent(new UserRoleAddedEvent(user, rol));
        return UserResponseDTO.fromUser(user);
    }


    private User getUserById(Long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new NotFoundException("user not found"));
    }

    // para usar internamente no en controller
    public void createDefaultAdmins() {
        log.info("Creando usuarios admin..");

        if (userRepository.existsByEmail(adminEmail)) {
            log.info("ya existe admin en bd. Se omite la creacion");
            return;
        }

        User admin = User.builder()
                .name(adminUserName)
                .password(passwordEncoder.encode(adminPassword))
                .email(adminEmail)
                .roles(new HashSet<>(List.of(Roles.ADMIN)))
                .build();

        userRepository.save(admin);
        log.info("Usuario admin por defecto creado");
    }

    @Transactional
    public UserResponseDTO createUser(UserCreateDTO createDTO) {
        var createdUser = authService.registerUser(createDTO);

        createdUser.getRoles().forEach(rol ->
                eventPublisher.publishEvent(new UserRoleAddedEvent(createdUser, rol))
        );

        return UserResponseDTO.fromUser(createdUser);
    }
}
