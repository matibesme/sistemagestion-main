package com.globallogic.sistema.gestion.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "alumnos")
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
public class Alumno extends BaseEntity{

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
