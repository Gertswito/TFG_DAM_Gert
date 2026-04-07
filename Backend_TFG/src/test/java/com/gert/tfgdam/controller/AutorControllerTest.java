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

import com.gert.tfgdam.entity.Autor;
import com.gert.tfgdam.service.AutorService;

@WebMvcTest(AutorController.class)
@AutoConfigureMockMvc(addFilters = false)
class AutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AutorService autorService;

    private Autor autor;

    @BeforeEach
    void inicializarAutor() {
        autor = new Autor();
        autor.setId(1);
        autor.setNombre("Autor Test");
    }

    @Test
    void getAllAutor_ok() throws Exception {
        when(autorService.getAllAutor()).thenReturn(List.of(autor));
        mockMvc.perform(get("/api/autor/get")).andExpect(status().isOk());
    }

    @Test
    void getAutorPorId_ok() throws Exception {
        when(autorService.getAutorPorId(1L)).thenReturn(autor);
        mockMvc.perform(get("/api/autor/get/1")).andExpect(status().isOk());
    }

    @Test
    void getAutorPorId_notFound() throws Exception {
        when(autorService.getAutorPorId(1L)).thenThrow(new RuntimeException());
        mockMvc.perform(get("/api/autor/get/1")).andExpect(status().isNotFound());
    }

    @Test
    void delete_ok() throws Exception {
        doNothing().when(autorService).delete(1L);
        mockMvc.perform(delete("/api/autor/delete/1")).andExpect(status().isNoContent());
    }

    @Test
    void create_ok() throws Exception {
        when(autorService.save(any())).thenReturn(autor);
        mockMvc.perform(post("/api/autor/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nombre": "Autor Test"
                        }
                        """))
                .andExpect(status().isCreated());
    }
}