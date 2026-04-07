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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gert.tfgdam.entity.TipoLibro;
import com.gert.tfgdam.service.TipoLibroService;

@WebMvcTest(TipoLibroController.class)
@AutoConfigureMockMvc(addFilters = false)
class TipoLibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TipoLibroService tipoLibroService;

    private TipoLibro tipoLibro;

    @BeforeEach
    void inicializarTipoLibro() {
        tipoLibro = new TipoLibro();
        tipoLibro.setId(1);
        tipoLibro.setNombre("TipoLibro Test");
    }

    @Test
    void getAllTipoLibro_ok() throws Exception {
        when(tipoLibroService.getAllTipoLibro()).thenReturn(List.of(tipoLibro));
        mockMvc.perform(get("/api/tipo-libro/get")).andExpect(status().isOk());
    }

    @Test
    void getTipoLibroPorId_ok() throws Exception {
        when(tipoLibroService.getTipoLibroPorId(1L)).thenReturn(tipoLibro);
        mockMvc.perform(get("/api/tipo-libro/get/1")).andExpect(status().isOk());
    }

    @Test
    void getTipoLibroPorId_notFound() throws Exception {
        when(tipoLibroService.getTipoLibroPorId(1L)).thenThrow(new RuntimeException());
        mockMvc.perform(get("/api/tipo-libro/get/1")).andExpect(status().isNotFound());
    }

    @Test
    void delete_ok() throws Exception {
        doNothing().when(tipoLibroService).delete(1L);
        mockMvc.perform(delete("/api/tipo-libro/delete/1")).andExpect(status().isNoContent());
    }

    @Test
    void create_ok() throws Exception {
        when(tipoLibroService.save(any())).thenReturn(tipoLibro);
        mockMvc.perform(post("/api/tipo-libro/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nombre": "TipoLibro Test"
                        }
                        """))
                .andExpect(status().isCreated());
    }
}