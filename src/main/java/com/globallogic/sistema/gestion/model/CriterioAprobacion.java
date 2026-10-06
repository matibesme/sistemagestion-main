package com.globallogic.sistema.gestion.model;

import jakarta.persistence.*;

@Entity
@Table(name = "criterios_aprobacion")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_criterio")
public abstract class CriterioAprobacion extends BaseEntity{
    public abstract boolean estaAprobado(Inscripcion inscripcion);
}
