package com.gert.tfgdam.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Direccion;
import com.gert.tfgdam.repository.DireccionRepository;
import com.gert.tfgdam.repository.VentaRepository;

@Service
public class DireccionService {
    private final DireccionRepository direccionRepository;

    private final VentaRepository ventaRepository;

    public DireccionService(DireccionRepository direccionRepository, VentaRepository ventaRepository) {
        this.direccionRepository = direccionRepository;
        this.ventaRepository = ventaRepository;
    }

    public List<Direccion> getAllDireccion() {
        return direccionRepository.findAll();
    }

    public List<Direccion> getAllDireccionPorClienteId(Long clienteId) {
        return direccionRepository.findByClienteId(clienteId);
    }

    public Direccion getDireccionPorId(Long id) {
        return direccionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la dirección"));
    }

    public void delete(Long id) {
        if (!direccionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la dirección");
        }
        if (ventaRepository.existsByDireccionId(id)) {
            Direccion direccionActualizada = direccionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la dirección"));
            direccionActualizada.setActivo(false);
            direccionRepository.save(direccionActualizada);
        } else {
            direccionRepository.deleteById(id);
        }
    }

    public Direccion save(Direccion direccion) {
        return direccionRepository.save(direccion);
    }

    public Direccion update(Direccion direccion) {
        return direccionRepository.save(direccion);
    }
}

