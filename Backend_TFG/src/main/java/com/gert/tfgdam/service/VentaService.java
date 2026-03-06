package com.gert.tfgdam.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.FinalizarCompra;
import com.gert.tfgdam.entity.Libro;
import com.gert.tfgdam.entity.LineaVenta;
import com.gert.tfgdam.entity.Venta;
import com.gert.tfgdam.repository.LibroRepository;
import com.gert.tfgdam.repository.LineaVentaRepository;
import com.gert.tfgdam.repository.VentaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class VentaService {
    private final VentaRepository ventaRepository;

    private final LineaVentaRepository lineaVentaRepository;

    private final LibroRepository libroRepository;

    private final EmailService emailService;

    public VentaService(VentaRepository ventaRepository, LineaVentaRepository lineaVentaRepository, LibroRepository libroRepository, EmailService emailService) {
        this.ventaRepository = ventaRepository;
        this.lineaVentaRepository = lineaVentaRepository;
        this.libroRepository = libroRepository;
        this.emailService = emailService;
    }

    public List<Venta> getAllVenta() {
        return ventaRepository.findAll();
    }

    public List<Venta> getAllVentaPorUsuario(String usuario) {
        return ventaRepository.findByClienteUsuario(usuario);
    }

    public Venta getVentaPorId(Long id) {
        return ventaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la venta"));
    }

    public void delete(Long id) {
        if (!ventaRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la venta");
        }
        try {
            ventaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta venta no puede ser borrada", e);
        }
    }

    public Venta save(Venta venta) {
        return ventaRepository.save(venta);
    }

    public Venta update(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Transactional
    public Venta finalizarCompra(FinalizarCompra finalizarCompra) {
        try {
            Venta venta = ventaRepository.save(finalizarCompra.getVenta());
            venta.setFecha(LocalDate.now());
            venta.setHora(LocalTime.now());
            venta = ventaRepository.save(venta);
            BigDecimal precioFinal = BigDecimal.ZERO;

            for (LineaVenta lineaVenta : finalizarCompra.getLineasVenta()) {
                Libro libro = libroRepository.findById(lineaVenta.getLibro().getId().longValue()).orElseThrow(() -> new EntityNotFoundException("Libro con ID " + lineaVenta.getLibro().getId() + " no encontrado"));

                if (libro.getStock() < lineaVenta.getCantidad()) {
                    throw new IllegalStateException("Stock insuficiente para el libro: " + libro.getTitulo());
                }

                libro.setStock(libro.getStock() - lineaVenta.getCantidad());
                libroRepository.save(libro);

                lineaVenta.setVenta(venta);
                precioFinal = precioFinal.add(lineaVenta.getPrecioTotal());
                lineaVentaRepository.save(lineaVenta);
            }

            venta.setPrecioFinal(precioFinal);
            return ventaRepository.save(venta);
        } catch (Exception e) {
            throw new RuntimeException("Error al finalizar la compra: " + e.getMessage(), e);
        }
    }

    public void enviarCorreoVenta(FinalizarCompra finalizarCompra) {
        emailService.enviarCorreoVenta(finalizarCompra);
    }
}
