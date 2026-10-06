package com.globallogic.sistema.gestion.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("promedio")
@NoArgsConstructor
@AllArgsConstructor
public class CriterioPromedio extends CriterioAprobacion{

    @Column
    private Double promedioMin;

    @Override
    public boolean estaAprobado(Inscripcion inscripcion){
        return inscripcion.getPromedio() > this.promedioMin;
    }
}
