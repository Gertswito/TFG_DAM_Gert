package com.gert.tfgdam.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Venta;
import com.gert.tfgdam.repository.VentaRepository;

@Service
public class VentaService {
    private final VentaRepository ventaRepository;

    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    public List<Venta> getAllVenta() {
        return ventaRepository.findAll();
    }

    public Venta getVentaPorId(Long id) {
        return ventaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "ventaNoExiste"));
    }

    public void delete(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ventaNoExiste");
        }
        try {
            ventaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ventaNoSePuedeEliminar", e);
        }
    }

    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    public Venta update(Venta venta) {
        return ventaRepository.save(venta);
    }
}
