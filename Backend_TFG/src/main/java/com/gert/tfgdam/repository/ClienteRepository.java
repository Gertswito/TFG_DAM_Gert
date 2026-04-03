package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.Cliente;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    @EntityGraph(attributePaths = {"direcciones"})
    Optional<Cliente> findWithDireccionesById(Integer id);

    @EntityGraph(attributePaths = {"direcciones"})
    Optional<Cliente> findWithDireccionesByUsuario(String usuario);

    Cliente findByUsuario(String usuario);
    
    boolean existsByUsuario(String usuario);

    boolean existsByEmail(String email);

    @Query("""
        SELECT c FROM Cliente c
        WHERE 
            LOWER(c.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(c.apellidos) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(c.usuario) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR CAST(c.id AS string) LIKE CONCAT('%', :texto, '%')
            OR LOWER(c.rol) LIKE LOWER(CONCAT('%', :texto, '%'))
    """)
    List<Cliente> findAllPorBusqueda(@Param("texto") String texto);
}
