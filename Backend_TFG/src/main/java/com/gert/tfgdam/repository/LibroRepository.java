package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.Libro;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    @EntityGraph(attributePaths = {
        "autor",
        "editorial",
        "tipoLibro",
        "generos"
    })
    List<Libro> findAll();

    boolean existsByTitulo(String titulo);
    
    Libro findByTitulo(String titulo);
}
