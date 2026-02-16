package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.TipoLibro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TipoLibroRepository extends JpaRepository<TipoLibro, Long> {
    boolean existsByNombre(String nombre);
    
    TipoLibro findByNombre(String nombre);
}
