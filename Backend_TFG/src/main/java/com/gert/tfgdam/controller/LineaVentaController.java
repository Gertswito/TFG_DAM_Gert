package com.gert.tfgdam.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.LineaVenta;
import com.gert.tfgdam.service.LineaVentaService;

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
@RequestMapping("/api/linea-venta")
public class LineaVentaController {
    private final LineaVentaService lineaVentaService;

    public LineaVentaController(LineaVentaService lineaVentaService) {
        this.lineaVentaService = lineaVentaService;
    }

    @GetMapping("/get")
    public List<LineaVenta> getAllLineaVenta() {
        return lineaVentaService.getAllLineaVenta();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<LineaVenta> getLineaVentaPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(lineaVentaService.getLineaVentaPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get/usuario/{usuario}")
    public List<LineaVenta> getAllLineaVentaPorUsuario(@PathVariable String usuario) {
        return lineaVentaService.getAllLineaVentaPorUsuario(usuario);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            lineaVentaService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }   

    @PostMapping("/new")
    public ResponseEntity<Object> create(@RequestBody LineaVenta lineaVenta) throws URISyntaxException {
        try {
            LineaVenta nuevaLineaVenta = lineaVentaService.save(lineaVenta);
            URI location = new URI("/new/" + nuevaLineaVenta.getId());
            return ResponseEntity.created(location).body(nuevaLineaVenta);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<LineaVenta> update(@PathVariable Long id, @RequestBody LineaVenta lineaVenta) {
        try {
            if (lineaVentaService.getLineaVentaPorId(id) != null) {
                lineaVentaService.update(lineaVenta);
                return ResponseEntity.ok(lineaVenta);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
