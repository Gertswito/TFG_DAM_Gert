package com.gert.tfgdam.controller;

import java.math.BigDecimal;
import java.util.List;

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

import com.gert.tfgdam.entity.Libro;
import com.gert.tfgdam.entity.LineaVenta;
import com.gert.tfgdam.entity.Venta;
import com.gert.tfgdam.service.LineaVentaService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LineaVentaController.class)
@AutoConfigureMockMvc(addFilters = false)
class LineaVentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LineaVentaService lineaVentaService;

    private LineaVenta lineaVenta;
    private Libro libro;
    private Venta venta;

    @BeforeEach
    void inicializarLineaVenta() {
        libro = new Libro();
        libro.setId(1);
        libro.setTitulo("Libro Test");

        venta = new Venta();
        venta.setId(1);

        lineaVenta = new LineaVenta();
        lineaVenta.setId(1);
        lineaVenta.setLibro(libro);
        lineaVenta.setVenta(venta);
        lineaVenta.setCantidad(2);
        lineaVenta.setPrecioParcial(new BigDecimal("10.0"));
        lineaVenta.setPrecioTotal(new BigDecimal("20.0"));
    }

    @Test
    void getAllLineaVenta_ok() throws Exception {
        when(lineaVentaService.getAllLineaVenta()).thenReturn(List.of(lineaVenta));

        mockMvc.perform(get("/api/linea-venta/get"))
                .andExpect(status().isOk());
    }

    @Test
    void getLineaVentaPorId_ok() throws Exception {
        when(lineaVentaService.getLineaVentaPorId(1L)).thenReturn(lineaVenta);

        mockMvc.perform(get("/api/linea-venta/get/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getLineaVentaPorId_notFound() throws Exception {
        when(lineaVentaService.getLineaVentaPorId(1L)).thenThrow(new RuntimeException());

        mockMvc.perform(get("/api/linea-venta/get/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllLineaVentaPorVenta_ok() throws Exception {
        when(lineaVentaService.getAllLineaVentaPorVenta(1L)).thenReturn(List.of(lineaVenta));

        mockMvc.perform(get("/api/linea-venta/get/venta/1"))
                .andExpect(status().isOk());
    }

    @Test
    void create_ok() throws Exception {
        when(lineaVentaService.save(any())).thenReturn(lineaVenta);

        mockMvc.perform(post("/api/linea-venta/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "cantidad": 2,
                            "precioParcial": 10.0,
                            "precioTotal": 20.0,
                            "libro": {"id": 1},
                            "venta": {"id": 1}
                        }
                        """))
                .andExpect(status().isCreated());
    }
}
