package com.globallogic.sistema.gestion.repository;

import com.globallogic.sistema.gestion.model.Tema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemaRepository extends JpaRepository<Tema, Long> {
}
