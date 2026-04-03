package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.Editorial;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EditorialRepository extends JpaRepository<Editorial, Long> {
    boolean existsByNombre(String nombre);

    Editorial findByNombre(String nombre);

    @Query("""
        SELECT e FROM Editorial e
        WHERE 
            LOWER(e.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR CAST(e.id AS string) LIKE CONCAT('%', :texto, '%')
    """)
    List<Editorial> findAllPorBusqueda(@Param("texto") String texto);
}

