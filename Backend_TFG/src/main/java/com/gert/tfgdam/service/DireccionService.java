package com.gert.tfgdam.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Direccion;
import com.gert.tfgdam.repository.DireccionRepository;

@Service
public class DireccionService {
    private final DireccionRepository direccionRepository;

    public DireccionService(DireccionRepository direccionRepository) {
        this.direccionRepository = direccionRepository;
    }

    public List<Direccion> getAllDireccion() {
        return direccionRepository.findAll();
    }

    public Direccion getDireccionPorId(Long id) {
        return direccionRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la dirección"));
    }

    public void delete(Long id) {
        if (!direccionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la dirección");
        }
        try {
            direccionRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta dirección no puede ser eliminada", e);
        }
    }

    public Direccion save(Direccion direccion) {
        return direccionRepository.save(direccion);
    }

    public Direccion update(Direccion direccion) {
        return direccionRepository.save(direccion);
    }
}

