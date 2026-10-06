package com.globallogic.sistema.gestion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "examenes")
public class Examen extends BaseEntity{

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

}
