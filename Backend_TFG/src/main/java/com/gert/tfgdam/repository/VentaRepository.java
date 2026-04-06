package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.Venta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByClienteUsuario(String usuario);

    @Query("""
        SELECT v FROM Venta v
        WHERE
            CAST(v.id AS string) LIKE CONCAT('%', :texto, '%')
            OR LOWER(v.cliente.usuario) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR LOWER(CONCAT(v.direccion.calle, ' ', v.direccion.numero, ', ', v.direccion.piso)) LIKE LOWER(CONCAT('%', :texto, '%'))
            OR CAST(v.fecha AS string) LIKE CONCAT('%', :texto, '%')
            OR CAST(v.hora AS string) LIKE CONCAT('%', :texto, '%')
            OR CAST(v.precioFinal AS string) LIKE CONCAT('%', :texto, '%')
    """)
    List<Venta> findAllPorBusqueda(@Param("texto") String texto);

    boolean existsByDireccionId(Long direccionId);
}

