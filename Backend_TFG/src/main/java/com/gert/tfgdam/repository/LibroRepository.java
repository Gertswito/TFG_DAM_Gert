package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.Libro;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    @EntityGraph(attributePaths = {
        "autor",
        "editorial",
        "tipoLibro",
        "generos"
    })
    List<Libro> findAll();

    @Query(value = """
        SELECT * 
        FROM libro l
        WHERE l.stock <= 20
        """, nativeQuery = true)
    List<Libro> findLibrosLimitadoPorStock();

    @Query(value = """
        SELECT * FROM (
            SELECT l.*,
                ROW_NUMBER() OVER (PARTITION BY l.tipolibro_id ORDER BY l.id) as rn
            FROM libro l
        ) t
        WHERE t.rn <= 5
        """, nativeQuery = true)
    List<Libro> findLibrosLimitadosYDivididosPorTipoLibro();

    List<Libro> findByTipoLibro_Nombre(String nombre);

    List<Libro> findByTipoLibro_NombreAndGeneros_Nombre(String tipoLibro, String genero);

    List<Libro> findByAutor_Nombre(String autor);

    boolean existsByTitulo(String titulo);
    
    Libro findByTitulo(String titulo);
}
