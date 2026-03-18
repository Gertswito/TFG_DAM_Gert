package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.Libro;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
    
    List<Libro> findByEditorial_Nombre(String editorial);

    @Query("SELECT l FROM Libro l WHERE MONTH(l.fechaSalida) = :mes AND YEAR(l.fechaSalida) = :yearEnIngles")
    List<Libro> findByMesActual(@Param("mes") int mes, @Param("yearEnIngles") int yearEnIngles);
    
    @Query(value = """
        SELECT * FROM libro l
        WHERE l.fecha_salida IS NULL OR NOT (
            MONTH(l.fecha_salida) = :mes 
            AND YEAR(l.fecha_salida) = :yearEnIngles
        )
        ORDER BY l.id DESC
        LIMIT 10
    """, nativeQuery = true)
    List<Libro> findTop10ExcluyendoMesActual(@Param("mes") int mes, @Param("yearEnIngles") int yearEnIngles);

    boolean existsByTitulo(String titulo);
    
    Libro findByTitulo(String titulo);
}
