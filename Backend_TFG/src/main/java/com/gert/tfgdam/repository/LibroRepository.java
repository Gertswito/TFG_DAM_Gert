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

    List<Libro> findByTipoLibro_Nombre(String nombre);

    List<Libro> findByTipoLibro_NombreAndGeneros_Nombre(String tipoLibro, String genero);

    boolean existsByTitulo(String titulo);
    
    Libro findByTitulo(String titulo);
}
