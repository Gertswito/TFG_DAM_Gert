package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.Cliente;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @EntityGraph(attributePaths = {"direcciones"})
    Optional<Cliente> findWithDireccionesById(Integer id);

    Cliente findByUsuario(String usuario);
    
    boolean existsByUsuario(String usuario);

    boolean existsByEmail(String email);
}
