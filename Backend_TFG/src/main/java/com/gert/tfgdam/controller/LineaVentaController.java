package com.gert.tfgdam.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.LineaVenta;
import com.gert.tfgdam.service.LineaVentaService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/get/venta/{ventaId}")
    public List<LineaVenta> getAllLineaVentaPorVenta(@PathVariable Long ventaId) {
        return lineaVentaService.getAllLineaVentaPorVenta(ventaId);
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
}
