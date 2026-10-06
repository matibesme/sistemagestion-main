package com.globallogic.sistema.gestion.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken extends BaseEntity {

    @Column(name = "token", nullable = false, unique = true, length = 500)
    private String token;

    @Column(nullable = false, name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(nullable = false, name = "revoked")
    private boolean revoked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public boolean isValid(@NonNull LocalDateTime now) {
        return !revoked && now.isBefore(this.expiresAt);
    }

    public void revoke() {
        this.revoked = true;
    }

    public static RefreshToken of(@NonNull String token, @NonNull LocalDateTime expiresAt, @NonNull User user) {
        RefreshToken newToken = new RefreshToken();
        newToken.setToken(token);
        newToken.setExpiresAt(expiresAt);
        newToken.setUser(user);
        return newToken;
    }
}
