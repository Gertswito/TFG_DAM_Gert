package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EditorialRepository extends JpaRepository<Editorial, Long> {
    boolean existsByNombre(String nombre);

    Editorial findByNombre(String nombre);
}

