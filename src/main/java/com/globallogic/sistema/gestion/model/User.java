package com.globallogic.sistema.gestion.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity representing a user in the system.
 * Used for authentication and authorization.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    @Builder.Default
    private Set<Roles> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    @Builder.Default
    private EstadoUsuario estado = EstadoUsuario.ACTIVO;

    @Builder.Default
    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts = 0;

    @Column(name = "blocked_until")
    private LocalDateTime lockedUntil;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PasswordResetToken> passwordResetTokens = new ArrayList<>();

    public void addRol(Roles rol){
        this.roles.add(rol);
    }

    public boolean tieneRol(Roles rol){
        return this.roles.contains(rol);
    }

    public Duration tiempoRestanteBloqueado(LocalDateTime now) {
        return estaBloqueado(now) ? Duration.between(now, lockedUntil) : Duration.ZERO;
    }

    public boolean estaBloqueado(LocalDateTime now) {
        if (this.lockedUntil == null) {
            return false;
        }
        if (this.lockedUntil.isAfter(now)) {
            return true;
        }

        this.desbloquear();
        return false;
    }

    private void desbloquear(){
        this.lockedUntil = null;
        this.failedLoginAttempts = 0;
    }

    public int registrarIntentoFallido(int maxIntentos, Duration duracionBloqueo, LocalDateTime now) {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= maxIntentos) {
            this.lockedUntil = now.plus(duracionBloqueo);
            return 0;
        }
        return maxIntentos - failedLoginAttempts;
    }
    public void registarLoginExitoso(){
        this.desbloquear();
    }
}
