package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    boolean existsByNombre(String nombre);
    
    Autor findByNombre(String nombre);
}
