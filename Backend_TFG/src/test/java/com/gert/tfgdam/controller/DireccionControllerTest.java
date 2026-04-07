package com.gert.tfgdam.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.gert.tfgdam.entity.Cliente;
import com.gert.tfgdam.entity.Direccion;
import com.gert.tfgdam.service.DireccionService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DireccionController.class)
@AutoConfigureMockMvc(addFilters = false)
class DireccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DireccionService direccionService;

    private Direccion direccion;
    private Cliente cliente;

    @BeforeEach
    void inicializarDireccion() {
        cliente = new Cliente();
        cliente.setId(1);
        cliente.setNombre("Cliente Test");

        direccion = new Direccion();
        direccion.setId(1);
        direccion.setCliente(cliente);
        direccion.setCalle("Calle Test");
        direccion.setNumero("123");
        direccion.setPiso("1A");
        direccion.setCiudad("Ciudad Test");
        direccion.setProvincia("Provincia Test");
        direccion.setCodigoPostal("28001");
        direccion.setActivo(true);
    }

    @Test
    void getAllDireccion_ok() throws Exception {
        when(direccionService.getAllDireccion()).thenReturn(List.of(direccion));
        mockMvc.perform(get("/api/direccion/get"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllDireccionPorClienteId_ok() throws Exception {
        when(direccionService.getAllDireccionPorClienteId(1L)).thenReturn(List.of(direccion));
        mockMvc.perform(get("/api/direccion/get/cliente/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getDireccionPorId_ok() throws Exception {
        when(direccionService.getDireccionPorId(1L)).thenReturn(direccion);
        mockMvc.perform(get("/api/direccion/get/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getDireccionPorId_notFound() throws Exception {
        when(direccionService.getDireccionPorId(1L)).thenThrow(new RuntimeException());
        mockMvc.perform(get("/api/direccion/get/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_ok() throws Exception {
        doNothing().when(direccionService).delete(1L);
        mockMvc.perform(delete("/api/direccion/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void create_ok() throws Exception {
        when(direccionService.save(any())).thenReturn(direccion);
        mockMvc.perform(post("/api/direccion/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "cliente": {"id": 1},
                                    "calle": "Calle Test",
                                    "numero": "123",
                                    "piso": "1A",
                                    "ciudad": "Ciudad Test",
                                    "provincia": "Provincia Test",
                                    "codigoPostal": "28001",
                                    "activo": true
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void update_ok() throws Exception {
        when(direccionService.getDireccionPorId(1L)).thenReturn(direccion);
        when(direccionService.update(any())).thenReturn(direccion);

        mockMvc.perform(put("/api/direccion/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "calle": "Calle Test",
                                    "numero": "123",
                                    "ciudad": "Ciudad Test",
                                    "provincia": "Provincia Test",
                                    "codigoPostal": "12345",
                                    "activo": true
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void update_notFound() throws Exception {
        when(direccionService.getDireccionPorId(1L)).thenReturn(null);

        mockMvc.perform(put("/api/direccion/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "cliente": {"id": 1},
                                    "calle": "Calle Actualizada",
                                    "numero": "123",
                                    "piso": "1A",
                                    "ciudad": "Ciudad Test",
                                    "provincia": "Provincia Test",
                                    "codigoPostal": "28001",
                                    "activo": true
                                }
                                """))
                .andExpect(status().isNotFound());
    }
}