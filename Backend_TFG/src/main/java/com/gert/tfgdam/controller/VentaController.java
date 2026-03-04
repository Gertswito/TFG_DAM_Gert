package com.gert.tfgdam.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.component.VentaTemporalEnMemoria;
import com.gert.tfgdam.entity.FinalizarCompra;
import com.gert.tfgdam.entity.Libro;
import com.gert.tfgdam.entity.LineaVenta;
import com.gert.tfgdam.entity.Venta;
import com.gert.tfgdam.repository.LibroRepository;
import com.gert.tfgdam.service.PayPalOrderService;
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

    private final LibroRepository libroRepository;

    private final PayPalOrderService payPalOrderService;

    private final VentaTemporalEnMemoria ventaTemporalEnMemoria;

    public VentaController(VentaService ventaService, LibroRepository libroRepository, PayPalOrderService payPalOrderService, VentaTemporalEnMemoria ventaTemporalEnMemoria) {
        this.ventaService = ventaService;
        this.libroRepository = libroRepository;
        this.payPalOrderService = payPalOrderService;
        this.ventaTemporalEnMemoria = ventaTemporalEnMemoria;
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

    @PostMapping("/paypal/create-order")
    public ResponseEntity<Object> createPaypalOrder(@RequestBody FinalizarCompra compra) {
        Map<String, Object> order = payPalOrderService.createOrder(compra.getVenta().getPrecioFinal().doubleValue());
        ventaTemporalEnMemoria.guardar(order.get("id").toString(), compra);
        return ResponseEntity.ok(order);
    }

    @PostMapping("/paypal/capture/{orderId}")
    public ResponseEntity<Object> capturePaypalOrder(@PathVariable String orderId) {
        Map<String, Object> capture = payPalOrderService.captureOrder(orderId);

        if (!"COMPLETED".equals(capture.get("status"))) {
            return ResponseEntity.badRequest().body("Pago no completado");
        }

        FinalizarCompra finalizarCompra = ventaTemporalEnMemoria.obtener(orderId);
        if (finalizarCompra == null) {
            return ResponseEntity.badRequest().body("No se encontró la venta temporal");
        }

        Venta venta = ventaService.finalizarCompra(finalizarCompra);
        ventaTemporalEnMemoria.eliminar(orderId);
        return ResponseEntity.ok(venta);
    }

    @PostMapping("/validar-stock")
    public ResponseEntity<Object> validarStock(@RequestBody List<LineaVenta> lineas) {
        for (LineaVenta linea : lineas) {
            Libro libro = libroRepository.findById(linea.getLibro().getId().longValue()).orElseThrow();

            if (libro.getStock() < linea.getCantidad()) {
                return ResponseEntity.badRequest().body("Stock insuficiente para " + libro.getTitulo());
            }
        }
        return ResponseEntity.ok().build();
    }
}