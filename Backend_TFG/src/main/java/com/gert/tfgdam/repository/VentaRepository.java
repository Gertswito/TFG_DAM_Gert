package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.Venta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findByClienteUsuario(String usuario);
}

