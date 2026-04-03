package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.Autor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    boolean existsByNombre(String nombre);
    
    Autor findByNombre(String nombre);

    @Query("""
        SELECT a FROM Autor a
        WHERE 
            LOWER(a.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR CAST(a.id AS string) LIKE CONCAT('%', :texto, '%')
    """)
    List<Autor> findAllPorBusqueda(@Param("texto") String texto);
}
