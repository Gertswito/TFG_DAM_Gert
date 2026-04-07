package com.gert.tfgdam.controller;

import java.math.BigDecimal;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.gert.tfgdam.entity.Autor;
import com.gert.tfgdam.entity.Cliente;
import com.gert.tfgdam.entity.Editorial;
import com.gert.tfgdam.entity.Libro;
import com.gert.tfgdam.entity.TipoLibro;
import com.gert.tfgdam.service.LibroService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibroController.class)
@AutoConfigureMockMvc(addFilters = false)
class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LibroService libroService;

    private Libro libro;
    private Autor autor;
    private Editorial editorial;
    private TipoLibro tipoLibro;

    @BeforeEach
    void inicializarLibro() {
        autor = new Autor();
        autor.setId(1);
        autor.setNombre("Autor Test");

        editorial = new Editorial();
        editorial.setId(1);
        editorial.setNombre("Editorial Test");

        tipoLibro = new TipoLibro();
        tipoLibro.setId(1);
        tipoLibro.setNombre("Tipo Test");

        libro = new Libro();
        libro.setId(1);
        libro.setTitulo("Libro Test");
        libro.setAutor(autor);
        libro.setEditorial(editorial);
        libro.setTipoLibro(tipoLibro);
        libro.setPrecio(BigDecimal.valueOf(19.99));
        libro.setStock(10);
        libro.setIsbn("123-4567890123");
    }

    @Test
    void getAllLibro_ok() throws Exception {
        when(libroService.getAllLibro()).thenReturn(List.of(libro));
        mockMvc.perform(get("/api/libro/get"))
                .andExpect(status().isOk());
    }

    @Test
    void getLibroPorId_ok() throws Exception {
        when(libroService.getLibroPorId(1L)).thenReturn(libro);
        mockMvc.perform(get("/api/libro/get/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getLibroPorId_notFound() throws Exception {
        when(libroService.getLibroPorId(1L)).thenThrow(new RuntimeException());
        mockMvc.perform(get("/api/libro/get/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_ok() throws Exception {
        doNothing().when(libroService).delete(1L);
        mockMvc.perform(delete("/api/libro/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void create_ok() throws Exception {
        when(libroService.save(any())).thenReturn(libro);
        mockMvc.perform(post("/api/libro/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "titulo": "Libro Test",
                            "isbn": "123-4567890123",
                            "precio": 19.99,
                            "stock": 10,
                            "autor": {"id":1},
                            "editorial": {"id":1},
                            "tipoLibro": {"id":1}
                        }
                        """))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllLibroLimitadoPorStock_ok() throws Exception {
        when(libroService.getAllLibroLimitadoPorStock()).thenReturn(List.of(libro));
        mockMvc.perform(get("/api/libro/get/stock"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllLibroLimitadoParaView_ok() throws Exception {
        when(libroService.getAllLibroLimitadoParaView()).thenReturn(List.of(libro));
        mockMvc.perform(get("/api/libro/get/view"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllLibroPorTipo_ok() throws Exception {
        when(libroService.getAllLibroPorTipo("Tipo Test")).thenReturn(List.of(libro));
        mockMvc.perform(get("/api/libro/get/tipo/Tipo Test"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllLibroPorTipoGenero_ok() throws Exception {
        when(libroService.getAllLibroPorTipoGenero("Tipo Test", "Genero Test")).thenReturn(List.of(libro));
        mockMvc.perform(get("/api/libro/get/tipo/Tipo Test/genero/Genero Test"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllPorNombreDeAutor_ok() throws Exception {
        when(libroService.getAllPorNombreDeAutor("Autor Test")).thenReturn(List.of(libro));
        mockMvc.perform(get("/api/libro/get/autor/Autor Test"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllPorNombreDeEditorial_ok() throws Exception {
        when(libroService.getAllPorNombreDeEditorial("Editorial Test")).thenReturn(List.of(libro));
        mockMvc.perform(get("/api/libro/get/editorial/Editorial Test"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllLibroNovedadesMes_ok() throws Exception {
        when(libroService.getAllLibroNovedadesMes()).thenReturn(List.of(libro));
        mockMvc.perform(get("/api/libro/get/novedades-mes"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllLibroNovedadesUltimaAdicion_ok() throws Exception {
        when(libroService.getAllLibroNovedadesUltimaAdicion()).thenReturn(List.of(libro));
        mockMvc.perform(get("/api/libro/get/novedades-latest"))
                .andExpect(status().isOk());
    }

    @Test
    void listaDeseados_add_and_delete_ok() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1);

        doNothing().when(libroService).addLibroListaDeseados(1L, cliente);
        mockMvc.perform(post("/api/libro/lista-deseados/add/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "id":1
                        }
                        """))
                .andExpect(status().isNoContent());

        doNothing().when(libroService).deleteLibroListaDeseados(1L,"usuarioTest");
        mockMvc.perform(delete("/api/libro/lista-deseados/delete/1/usuarioTest"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateLibro_ok() throws Exception {
        when(libroService.getLibroPorId(1L)).thenReturn(libro);
        when(libroService.update(any())).thenReturn(libro); 
        mockMvc.perform(put("/api/libro/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "titulo": "Libro Test Actualizado",
                            "precio": 25.0,
                            "stock": 15
                        }
                        """))
                .andExpect(status().isOk());
    }

    @Test
    void actualizarStock_ok() throws Exception {
        when(libroService.getLibroPorId(1L)).thenReturn(libro);

        when(libroService.actualizarStock(anyLong(), anyInt())).thenReturn(libro);

        mockMvc.perform(put("/api/libro/update/stock/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("5"))
                .andExpect(status().isOk());
    }
}
