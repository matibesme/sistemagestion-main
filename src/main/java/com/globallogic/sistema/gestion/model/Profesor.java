package com.globallogic.sistema.gestion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "profesores")
@Getter
public class Profesor extends BaseEntity{

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private LocalDateTime fechaDeAlta;

    @ManyToMany(mappedBy = "profesoresACargo")
    private List<Curso> cursosAsignados = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public Profesor(User user, LocalDateTime now){
        this.nombre = user.getName();
        this.user = user;
        this.fechaDeAlta = now;
    }

    public void agregarCurso(Curso ...curso){
        this.cursosAsignados.addAll(Arrays.asList(curso));
    }

    public int getCantCursos() { return this.cursosAsignados.size(); }
}
