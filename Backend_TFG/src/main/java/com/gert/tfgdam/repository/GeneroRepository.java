package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.Genero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneroRepository extends JpaRepository<Genero, Long> {
    boolean existsByNombre(String nombre);
    
    Genero findByNombre(String nombre);
}
