package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.Direccion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DireccionRepository extends JpaRepository<Direccion, Long> {
    List<Direccion> findByClienteId(Long clienteId);
}
