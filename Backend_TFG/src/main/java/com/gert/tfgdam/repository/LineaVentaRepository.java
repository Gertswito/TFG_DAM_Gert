package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.LineaVenta;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LineaVentaRepository extends JpaRepository<LineaVenta, Long> {
    List<LineaVenta> findByVentaId(Long ventaId);
}
