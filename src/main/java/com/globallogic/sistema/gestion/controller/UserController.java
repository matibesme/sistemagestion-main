package com.globallogic.sistema.gestion.controller;

import com.globallogic.sistema.gestion.dto.AddRoleDTO;
import com.globallogic.sistema.gestion.dto.PagedResponseDTO;
import com.globallogic.sistema.gestion.dto.UpdateUserDTO;
import com.globallogic.sistema.gestion.dto.user.UserCreateDTO;
import com.globallogic.sistema.gestion.dto.user.UserResponseDTO;
import com.globallogic.sistema.gestion.model.Roles;
import com.globallogic.sistema.gestion.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<PagedResponseDTO<UserResponseDTO>> getUserList(@RequestParam(required = false) Roles rol,
                                                                         @PageableDefault(
                                                                                 sort = "id",
                                                                                 direction = Sort.Direction.ASC

                                                                         ) Pageable pageable) {
        var users = this.userService.getUsersByRole(rol, pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO user = this.userService.getUserResponseById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserDTO data) {
        UserResponseDTO user = this.userService.updateUser(data, id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/roles")
    public ResponseEntity<UserResponseDTO> addRole(@PathVariable Long id, @RequestBody @Valid AddRoleDTO rol) {
        UserResponseDTO updatedUser = this.userService.addRole(id, rol.rol());
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserCreateDTO createDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(createDTO));
    }

}
