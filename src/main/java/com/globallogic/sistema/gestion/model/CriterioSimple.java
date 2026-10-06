package com.globallogic.sistema.gestion.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("simple")
public class CriterioSimple extends CriterioAprobacion{

    @Column(name = "valor_minimo", nullable = false)
    private Double valorMinimo;

    @Override
    public boolean estaAprobado(Inscripcion inscripcion){
        return inscripcion.getExamenesRendidos()
                .stream()
                .anyMatch(e -> e.getNota() >= valorMinimo);
    }
}
