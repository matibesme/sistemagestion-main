package com.globallogic.sistema.gestion.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("combinado")
public class CriterioCombinado extends CriterioAprobacion{

    @ManyToMany
    @JoinTable(
            name = "criterio_jerarquia",
            joinColumns = @JoinColumn(name = "padre_id"),
            inverseJoinColumns = @JoinColumn(name = "hijo_id")
    )
    private List<CriterioAprobacion> criterios;

    @Override
    public boolean estaAprobado(Inscripcion inscripcion){
        return this.criterios
                .stream()
                .allMatch(c -> c.estaAprobado(inscripcion));
    }
}
