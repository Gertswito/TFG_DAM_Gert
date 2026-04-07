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

import com.gert.tfgdam.entity.Editorial;
import com.gert.tfgdam.service.EditorialService;

@WebMvcTest(EditorialController.class)
@AutoConfigureMockMvc(addFilters = false)
class EditorialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EditorialService editorialService;

    private Editorial editorial;

    @BeforeEach
    void inicializarEditorial() {
        editorial = new Editorial();
        editorial.setId(1);
        editorial.setNombre("Editorial Test");
    }

    @Test
    void getAllEditorial_ok() throws Exception {
        when(editorialService.getAllEditorial()).thenReturn(List.of(editorial));
        mockMvc.perform(get("/api/editorial/get")).andExpect(status().isOk());
    }

    @Test
    void getEditorialPorId_ok() throws Exception {
        when(editorialService.getEditorialPorId(1L)).thenReturn(editorial);
        mockMvc.perform(get("/api/editorial/get/1")).andExpect(status().isOk());
    }

    @Test
    void getEditorialPorId_notFound() throws Exception {
        when(editorialService.getEditorialPorId(1L)).thenThrow(new RuntimeException());
        mockMvc.perform(get("/api/editorial/get/1")).andExpect(status().isNotFound());
    }

    @Test
    void delete_ok() throws Exception {
        doNothing().when(editorialService).delete(1L);
        mockMvc.perform(delete("/api/editorial/delete/1")).andExpect(status().isNoContent());
    }

    @Test
    void create_ok() throws Exception {
        when(editorialService.save(any())).thenReturn(editorial);
        mockMvc.perform(post("/api/editorial/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "nombre": "Editorial Test"
                        }
                        """))
                .andExpect(status().isCreated());
    }
}