package com.globallogic.sistema.gestion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "examenes_rendidos")
public class ExamenRendido extends BaseEntity{

        @JoinColumn(name = "examen_id", nullable = false)
        @ManyToOne(optional = false)
        private Examen examen;

        @Column(name = "nota")
        private Double nota;

        @Column(name = "fecha_rendido")
        private LocalDateTime fechaRendido;

        @ManyToOne(optional = false)
        @JoinColumn(name = "id_alumno", nullable = false)
        private Alumno alumno;
}
