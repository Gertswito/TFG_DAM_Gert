package com.gert.tfgdam.service;

import com.gert.tfgdam.repository.TipoLibroRepository;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Libro;
import com.gert.tfgdam.entity.LineaVenta;
import com.gert.tfgdam.entity.Venta;
import com.gert.tfgdam.repository.LibroRepository;
import com.gert.tfgdam.repository.LineaVentaRepository;
import com.gert.tfgdam.repository.VentaRepository;

import jakarta.transaction.Transactional;

@Service
public class LineaVentaService {
    private final LineaVentaRepository lineaVentaRepository;
    private final VentaRepository ventaRepository;
    private final LibroRepository libroRepository;

    public LineaVentaService(LineaVentaRepository lineaVentaRepository, VentaRepository ventaRepository, TipoLibroRepository tipoLibroRepository, LibroRepository libroRepository) {
        this.lineaVentaRepository = lineaVentaRepository;
        this.ventaRepository = ventaRepository;
        this.libroRepository = libroRepository;
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

    @Transactional
    public LineaVenta save(LineaVenta lineaVenta) {
        Libro libro = libroRepository.findById(lineaVenta.getLibro().getId().longValue()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el libro"));
        if ((libro.getStock() - lineaVenta.getCantidad()) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stock insuficiente para el libro: " + libro.getTitulo());
        }
        libro.setStock(libro.getStock() - lineaVenta.getCantidad());
        libroRepository.save(libro);

        Venta venta = ventaRepository.findById(lineaVenta.getVenta().getId().longValue()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la venta"));
        venta.setPrecioFinal(venta.getPrecioFinal().add(lineaVenta.getPrecioTotal()));
        ventaRepository.save(venta);

        return lineaVentaRepository.save(lineaVenta);
    }

    public LineaVenta update(LineaVenta lineaVenta) {
        return lineaVentaRepository.save(lineaVenta);
    }
}
