package com.evaluacion.agendamedica.repository;

import com.evaluacion.agendamedica.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Fundamental para asignar roles al registrar un usuario
    Optional<Role> findByNombre(String nombre);
}