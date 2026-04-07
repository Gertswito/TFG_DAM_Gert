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

import com.gert.tfgdam.entity.Genero;
import com.gert.tfgdam.service.GeneroService;

@WebMvcTest(GeneroController.class)
@AutoConfigureMockMvc(addFilters = false)
class GeneroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GeneroService generoService;

    private Genero genero;

    @BeforeEach
    void inicializarGenero() {
        genero = new Genero();
        genero.setId(1);
        genero.setNombre("Genero Test");
    }

    @Test
    void getAllGenero_ok() throws Exception {
        when(generoService.getAllGenero()).thenReturn(List.of(genero));
        mockMvc.perform(get("/api/genero/get")).andExpect(status().isOk());
    }

    @Test
    void getGeneroPorId_ok() throws Exception {
        when(generoService.getGeneroPorId(1L)).thenReturn(genero);
        mockMvc.perform(get("/api/genero/get/1")).andExpect(status().isOk());
    }

    @Test
    void getGeneroPorId_notFound() throws Exception {
        when(generoService.getGeneroPorId(1L)).thenThrow(new RuntimeException());
        mockMvc.perform(get("/api/genero/get/1")).andExpect(status().isNotFound());
    }

    @Test
    void delete_ok() throws Exception {
        doNothing().when(generoService).delete(1L);
        mockMvc.perform(delete("/api/genero/delete/1")).andExpect(status().isNoContent());
    }

    @Test
    void create_ok() throws Exception {
        when(generoService.save(any())).thenReturn(genero);
        mockMvc.perform(post("/api/genero/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nombre": "Genero Test"
                        }
                        """))
                .andExpect(status().isCreated());
    }
}