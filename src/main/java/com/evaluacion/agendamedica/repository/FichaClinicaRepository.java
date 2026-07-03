package com.evaluacion.agendamedica.repository;

import com.evaluacion.agendamedica.entity.FichaClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FichaClinicaRepository extends JpaRepository<FichaClinica, Integer> {
    // Fundamental para evitar violaciones de la restricción OneToOne
    boolean existsByCitaId(Integer citaId);
}
