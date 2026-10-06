package com.globallogic.sistema.gestion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "temas")
@AllArgsConstructor
public class Tema extends BaseEntity {
    @Column(name = "titulo", nullable = false)
    private String titulo;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "duracion", nullable = false)
    private Double duracion;
}
