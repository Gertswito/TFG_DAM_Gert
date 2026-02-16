package com.gert.tfgdam.repository;

import com.gert.tfgdam.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByUsuario(String usuario);
    
    boolean existsByUsuario(String usuario);

    boolean existsByDni(String dni);

    boolean existsByEmail(String email);
}
