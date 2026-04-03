package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.TipoLibro;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TipoLibroRepository extends JpaRepository<TipoLibro, Long> {
    boolean existsByNombre(String nombre);
    
    TipoLibro findByNombre(String nombre);

    @Query("""
        SELECT tl FROM TipoLibro tl
        WHERE 
            LOWER(tl.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR CAST(tl.id AS string) LIKE CONCAT('%', :texto, '%')
    """)
    List<TipoLibro> findAllPorBusqueda(@Param("texto") String texto);
}
