package com.globallogic.sistema.gestion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "cursos")
public class Curso extends BaseEntity {
    @Column(name = "nombre", nullable = false)
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "curso_profesor",
            joinColumns = @JoinColumn(name = "curso_id"),
            inverseJoinColumns = @JoinColumn(name = "profesor_id")
    )
    private List<Profesor> profesoresACargo = new ArrayList<>();
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.PERSIST})
    private List<Tema> temas = new ArrayList<>();

    @OneToMany(mappedBy = "curso", cascade = {CascadeType.ALL})
    private List<Examen> evaluaciones;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "criterio_aprobacion_id", referencedColumnName = "id")
    private CriterioAprobacion criterioAprobacion;

    @Column
    private LocalDateTime fechaInicio;
    @Column
    private LocalDateTime fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoCurso estado = EstadoCurso.DISPONIBLE;

    public void agregarProfesores(List<Profesor> profesores){
        for (var profesor: profesores){
            this.profesoresACargo.add(profesor);
            profesor.agregarCurso(this);
        }
    }

    public void agregarTema(@NonNull Tema tema){
        this.temas.add(tema);
    }

    public void addExamen(Examen examen) {
        evaluaciones.add(examen);
        examen.setCurso(this);
    }

    public int cantTemas(){
        return this.temas.size();
    }

    public int cantEvaluaciones(){
        return this.evaluaciones.size();
    }

    public double duracion() {
        return this.temas.stream()
                .mapToDouble(Tema::getDuracion)
                .sum();
    }

    public boolean aprueba(Inscripcion inscripcion){
        return this.criterioAprobacion.estaAprobado(inscripcion);
    }

}
