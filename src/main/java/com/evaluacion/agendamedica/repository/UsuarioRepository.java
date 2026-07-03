package com.evaluacion.agendamedica.repository;

import com.evaluacion.agendamedica.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Fundamental para el UserDetailsService de Spring Security
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);
}
