package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.Genero;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GeneroRepository extends JpaRepository<Genero, Long> {
    boolean existsByNombre(String nombre);
    
    Genero findByNombre(String nombre);

    @Query("""
        SELECT g FROM Genero g
        WHERE 
            LOWER(g.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR CAST(g.id AS string) LIKE CONCAT('%', :texto, '%')
    """)
    List<Genero> findAllPorBusqueda(@Param("texto") String texto);
}
