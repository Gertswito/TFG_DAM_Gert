package com.gert.tfgdam.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.FinalizarCompra;
import com.gert.tfgdam.entity.Venta;
import com.gert.tfgdam.service.VentaService;

import jakarta.persistence.EntityNotFoundException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/venta")
public class VentaController {
    private final VentaService ventaService;

    public VentaController(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @GetMapping("/get")
    public List<Venta> getAllVenta() {
        return ventaService.getAllVenta();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Venta> getVentaPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ventaService.getVentaPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            ventaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }   

    @PostMapping("/new")
    public ResponseEntity<Object> create(@RequestBody Venta venta) throws URISyntaxException {
        try {
            Venta nuevaVenta = ventaService.save(venta);
            URI location = new URI("/new/" + nuevaVenta.getId());
            return ResponseEntity.created(location).body(nuevaVenta);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Venta> update(@PathVariable Long id, @RequestBody Venta venta) {
        try {
            if (ventaService.getVentaPorId(id) != null) {
                ventaService.update(venta);
                return ResponseEntity.ok(venta);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/finalizar-compra")
    public ResponseEntity<Object> finalizarCompra(@RequestBody FinalizarCompra finalizarCompra) {
        try {
            Venta nuevaVenta = ventaService.finalizarCompra(finalizarCompra);
            URI location = URI.create("/new/" + nuevaVenta.getId());
            return ResponseEntity.created(location).body(nuevaVenta);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}