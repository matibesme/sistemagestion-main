package com.globallogic.sistema.gestion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "inscripciones")
public class Inscripcion extends BaseEntity {

    @Setter
    @Getter
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @Setter
    @Getter
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;

    @Column(name = "fecha_inscripcion")
    private LocalDateTime fechaDeInscripcion;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<ExamenRendido> examenesRendidos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoInscripcion estado = EstadoInscripcion.EN_CURSO;

    public List<ExamenRendido> getExamenesRendidos() {
        return Collections.unmodifiableList(this.examenesRendidos);
    }

    public Double getPromedio() {
        return this.examenesRendidos
                .stream()
                .mapToDouble(ExamenRendido::getNota)
                .average()
                .orElse(0.0);
    }

    public void rendirExamen(@NonNull Examen examen, @NonNull Double nota, @NonNull LocalDateTime now) {

        if (this.estado != EstadoInscripcion.EN_CURSO)
            throw new IllegalStateException("Este alumno ya finalizo este curso");

        var examenRendido = new ExamenRendido();
        examenRendido.setAlumno(this.alumno);
        examenRendido.setNota(nota);
        examenRendido.setCreatedAt(now);
        examenRendido.setExamen(examen);
        this.examenesRendidos.add(examenRendido);

        if (this.finalizoCurso()) {
            if (this.curso.aprueba(this)) {
                this.estado = EstadoInscripcion.APROBADA;
            } else {
                this.estado = EstadoInscripcion.DESAPROBADA;
            }
        }
    }

    private boolean finalizoCurso() {
        return this.examenesRendidos.size() == this.curso.cantEvaluaciones();
    }
}
