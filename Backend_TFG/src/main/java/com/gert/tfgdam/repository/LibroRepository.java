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
            WHERE l.stock > 0
        ) t
        WHERE t.rn <= 5
        """, nativeQuery = true)
    List<Libro> findLibrosLimitadosYDivididosPorTipoLibro();

    List<Libro> findByTipoLibro_NombreAndStockGreaterThan(String nombre, int stock);

    List<Libro> findByTipoLibro_NombreAndGeneros_NombreAndStockGreaterThan(String tipoLibro, String genero, int stock);

    List<Libro> findByAutor_NombreAndStockGreaterThan(String autor, int stock);

    List<Libro> findByEditorial_NombreAndStockGreaterThan(String editorial, int stock);

    @Query("SELECT l FROM Libro l WHERE MONTH(l.fechaSalida) = :mes AND YEAR(l.fechaSalida) = :yearEnIngles AND l.stock > 0")
    List<Libro> findByMesActual(@Param("mes") int mes, @Param("yearEnIngles") int yearEnIngles);

    @Query(value = """
        SELECT * FROM libro l
        WHERE l.stock > 0
        AND (
            l.fecha_salida IS NULL OR NOT (
                MONTH(l.fecha_salida) = :mes 
                AND YEAR(l.fecha_salida) = :yearEnIngles
            )
        )
        ORDER BY l.id DESC
        LIMIT 10
    """, nativeQuery = true)
    List<Libro> findTop10ExcluyendoMesActual(@Param("mes") int mes, @Param("yearEnIngles") int yearEnIngles);

    boolean existsByTitulo(String titulo);
    
    Libro findByTitulo(String titulo);

    @Query("""
        SELECT l FROM Libro l
        WHERE 
            CAST(l.id AS string) LIKE CONCAT('%', :texto, '%')
            OR LOWER(l.isbn) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(l.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(l.editorial.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(l.autor.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(l.tipoLibro.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR CAST(l.fechaSalida AS string) LIKE CONCAT('%', :texto, '%')
            OR CAST(l.precio AS string) LIKE CONCAT('%', :texto, '%')
            OR CAST(l.stock AS string) LIKE CONCAT('%', :texto, '%')
    """)
    List<Libro> findAllPorBusqueda(@Param("texto") String texto);

    @Query("""
        SELECT l FROM Libro l
        WHERE l.stock > 0
        AND (
            LOWER(l.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(l.editorial.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(l.autor.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(l.tipoLibro.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
        )
    """)
    List<Libro> findAllPorBusquedaUser(@Param("texto") String texto);

    @Query("""
        SELECT DISTINCT l FROM Libro l
        LEFT JOIN l.generos g
        WHERE l.stock > 0
        AND LOWER(l.tipoLibro.nombre) = LOWER(:tipoLibroNombre)
        AND (
            LOWER(l.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(l.editorial.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(l.autor.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(g.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
        )
    """)
    List<Libro> findAllPorBusquedaTipo(@Param("tipoLibroNombre") String tipoLibroNombre, @Param("texto") String texto);

    @Query("""
        SELECT DISTINCT l FROM Libro l
        LEFT JOIN l.generos g
        WHERE l.stock > 0
        AND LOWER(l.tipoLibro.nombre) = LOWER(:tipoLibroNombre)
        AND LOWER(g.nombre) = LOWER(:generoNombre)
        AND (
            LOWER(l.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(l.editorial.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(l.autor.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
        )
    """)
    List<Libro> findAllPorBusquedaTipoGenero(@Param("tipoLibroNombre") String tipoLibroNombre, @Param("generoNombre") String generoNombre, @Param("texto") String texto);

    @Query("""
        SELECT l FROM Libro l
        WHERE l.stock > 0
        AND LOWER(l.autor.nombre) = LOWER(:autor)
        AND (
            LOWER(l.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(l.editorial.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(l.autor.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
        )
    """)
    List<Libro> findAllPorAutorBusqueda(@Param("autor") String autor, @Param("texto") String texto);

    @Query("""
        SELECT l FROM Libro l
        WHERE l.stock > 0
        AND LOWER(l.editorial.nombre) = LOWER(:editorial)
        AND (
            LOWER(l.titulo) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(l.editorial.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(l.autor.nombre) LIKE LOWER(CONCAT('%', :texto, '%'))
        )
    """)
    List<Libro> findAllPorEditorialBusqueda(@Param("editorial") String editorial, @Param("texto") String texto);
}
