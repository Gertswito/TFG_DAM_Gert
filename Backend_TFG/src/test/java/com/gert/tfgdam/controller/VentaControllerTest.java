package com.gert.tfgdam.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.gert.tfgdam.component.VentaTemporalEnMemoria;
import com.gert.tfgdam.entity.FinalizarCompra;
import com.gert.tfgdam.entity.Libro;
import com.gert.tfgdam.entity.LineaVenta;
import com.gert.tfgdam.entity.Venta;
import com.gert.tfgdam.repository.LibroRepository;
import com.gert.tfgdam.service.PayPalOrderService;
import com.gert.tfgdam.service.VentaService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@WebMvcTest(VentaController.class)
@AutoConfigureMockMvc(addFilters = false)
class VentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VentaService ventaService;

    @MockitoBean
    private LibroRepository libroRepository;

    @MockitoBean
    private PayPalOrderService payPalOrderService;

    @MockitoBean
    private VentaTemporalEnMemoria ventaTemporalEnMemoria;

    private Venta venta;
    private Libro libro;

    @BeforeEach
    void inicializarDatos() {
        libro = new Libro();
        libro.setId(1);
        libro.setTitulo("Libro Test");
        libro.setStock(10);

        venta = new Venta();
        venta.setId(1);
        venta.setPrecioFinal(BigDecimal.valueOf(100));
    }

    @Test
    void getAllVenta_ok() throws Exception {
        when(ventaService.getAllVenta()).thenReturn(List.of(venta));

        mockMvc.perform(get("/api/venta/get"))
                .andExpect(status().isOk());
    }

    @Test
    void getVentaPorId_ok() throws Exception {
        when(ventaService.getVentaPorId(1L)).thenReturn(venta);

        mockMvc.perform(get("/api/venta/get/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getVentaPorId_notFound() throws Exception {
        when(ventaService.getVentaPorId(1L)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/venta/get/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllVentaPorUsuario_ok() throws Exception {
        when(ventaService.getAllVentaPorUsuario("usuario1")).thenReturn(List.of(venta));

        mockMvc.perform(get("/api/venta/get/usuario/usuario1"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllVentaPorBusqueda_ok() throws Exception {
        when(ventaService.getAllVentaPorBusqueda("texto")).thenReturn(List.of(venta));

        mockMvc.perform(get("/api/venta/get/busqueda/texto"))
                .andExpect(status().isOk());
    }

    @Test
    void createVenta_ok() throws Exception {
        when(ventaService.save(any())).thenReturn(venta);

        mockMvc.perform(post("/api/venta/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "precioFinal": 100
                        }
                        """))
                .andExpect(status().isCreated());
    }

    @Test
    void validarStock_ok() throws Exception {
        LineaVenta linea = new LineaVenta();
        linea.setCantidad(5);
        linea.setLibro(libro);

        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));

        mockMvc.perform(post("/api/venta/validar-stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        [
                            {
                                "cantidad": 5,
                                "libro": {"id": 1}
                            }
                        ]
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void validarStock_insuficiente() throws Exception {
        LineaVenta linea = new LineaVenta();
        linea.setCantidad(15); 
        linea.setLibro(libro);

        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));

        mockMvc.perform(post("/api/venta/validar-stock")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        [
                            {
                                "cantidad": 15,
                                "libro": {"id": 1}
                            }
                        ]
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPaypalOrder_ok() throws Exception {
        Map<String, Object> order = Map.of("id", "ORDER123", "status", "CREATED");
        when(payPalOrderService.createOrder(100.0)).thenReturn(order);

        mockMvc.perform(post("/api/venta/paypal/create-order")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "venta": {"precioFinal": 100}
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void capturePaypalOrder_ok() throws Exception {
        Map<String, Object> capture = Map.of("status", "COMPLETED");
        when(payPalOrderService.captureOrder("ORDER123")).thenReturn(capture);
        FinalizarCompra compra = new FinalizarCompra();
        compra.setVenta(venta);
        when(ventaTemporalEnMemoria.obtener("ORDER123")).thenReturn(compra);
        when(ventaService.finalizarCompra(compra)).thenReturn(venta);

        mockMvc.perform(post("/api/venta/paypal/capture/ORDER123"))
                .andExpect(status().isOk());
    }

    @Test
    void capturePaypalOrder_noCompleto() throws Exception {
        Map<String, Object> capture = Map.of("status", "PENDING");
        when(payPalOrderService.captureOrder("ORDER123")).thenReturn(capture);

        mockMvc.perform(post("/api/venta/paypal/capture/ORDER123"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void capturePaypalOrder_noExisteVentaTemporal() throws Exception {
        Map<String, Object> capture = Map.of("status", "COMPLETED");
        when(payPalOrderService.captureOrder("ORDER123")).thenReturn(capture);
        when(ventaTemporalEnMemoria.obtener("ORDER123")).thenReturn(null);

        mockMvc.perform(post("/api/venta/paypal/capture/ORDER123"))
                .andExpect(status().isBadRequest());
    }
}
