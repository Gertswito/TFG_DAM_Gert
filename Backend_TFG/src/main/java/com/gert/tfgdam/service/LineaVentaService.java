package com.gert.tfgdam.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.LineaVenta;
import com.gert.tfgdam.repository.LineaVentaRepository;

@Service
public class LineaVentaService {
    private final LineaVentaRepository lineaVentaRepository;

    public LineaVentaService(LineaVentaRepository lineaVentaRepository) {
        this.lineaVentaRepository = lineaVentaRepository;
    }

    public List<LineaVenta> getAllLineaVenta() {
        return lineaVentaRepository.findAll();
    }

    public LineaVenta getLineaVentaPorId(Long id) {
        return lineaVentaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la línea de venta"));
    }

    public List<LineaVenta> getAllLineaVentaPorVenta(Long ventaId) {
        return lineaVentaRepository.findByVentaId(ventaId);
    }

    public void delete(Long id) {
        if (!lineaVentaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la línea de venta");
        }
        try {
            lineaVentaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta línea de venta no puede ser borrada", e);
        }
    }

    public LineaVenta save(LineaVenta lineaVenta) {
        return lineaVentaRepository.save(lineaVenta);
    }

    public LineaVenta update(LineaVenta lineaVenta) {
        return lineaVentaRepository.save(lineaVenta);
    }
}
