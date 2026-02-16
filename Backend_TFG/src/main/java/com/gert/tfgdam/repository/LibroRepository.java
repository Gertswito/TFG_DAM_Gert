package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    boolean existsByTitulo(String titulo);
    
    Libro findByTitulo(String titulo);
}
