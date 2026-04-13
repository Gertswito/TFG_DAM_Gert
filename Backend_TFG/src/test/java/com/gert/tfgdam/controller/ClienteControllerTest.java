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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.gert.tfgdam.entity.Cliente;
import com.gert.tfgdam.service.ClienteService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void inicializarCliente() {
        cliente = new Cliente();
        cliente.setId(1);
        cliente.setNombre("Nombre Test");
        cliente.setApellidos("Apellidos Test");
        cliente.setUsuario("usuarioTest");
        cliente.setEmail("test@email.com");
        cliente.setContrasenha("12345");
    }

    @Test
    void getAllCliente_ok() throws Exception {
        when(clienteService.getAllCliente()).thenReturn(List.of(cliente));
        mockMvc.perform(get("/api/cliente/get"))
                .andExpect(status().isOk());
    }

    @Test
    void getClientePorId_ok() throws Exception {
        when(clienteService.getClientePorId(1L)).thenReturn(cliente);
        mockMvc.perform(get("/api/cliente/get/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getClientePorId_notFound() throws Exception {
        when(clienteService.getClientePorId(1L)).thenThrow(new RuntimeException());
        mockMvc.perform(get("/api/cliente/get/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getClientePorUsuario_ok() throws Exception {
        when(clienteService.getClientePorUsuario("usuarioTest")).thenReturn(cliente);
        mockMvc.perform(get("/api/cliente/get/usuario/usuarioTest"))
                .andExpect(status().isOk());
    }

    @Test
    void getClientePorUsuario_notFound() throws Exception {
        when(clienteService.getClientePorUsuario("usuarioTest")).thenThrow(new RuntimeException());
        mockMvc.perform(get("/api/cliente/get/usuario/usuarioTest"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllClientePorBusqueda_ok() throws Exception {
        when(clienteService.getAllClientePorBusqueda("Test")).thenReturn(List.of(cliente));
        mockMvc.perform(get("/api/cliente/get/busqueda/Test"))
                .andExpect(status().isOk());
    }

    @Test
    void login_ok() throws Exception {
        when(clienteService.login(any())).thenReturn("token123");
        mockMvc.perform(post("/api/cliente/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "usuario": "usuarioTest",
                                    "contrasenha": "12345"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void cambiarContrasenha_ok() throws Exception {
        when(clienteService.getClientePorId(1L)).thenReturn(cliente);
        when(clienteService.cambiarContrasenha(eq(1L), anyString())).thenReturn(cliente);
        doNothing().when(clienteService).enviarCorreoCambioContrasenha(any());
        
        mockMvc.perform(put("/api/cliente/cambiarContrasenha/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"nuevaPass123\""))
                .andExpect(status().isOk());
    }

    @Test
    void cambiarContrasenha_notFound() throws Exception {
        when(clienteService.getClientePorId(1L)).thenReturn(null);
        doNothing().when(clienteService).enviarCorreoCambioContrasenha(any());

        mockMvc.perform(put("/api/cliente/cambiarContrasenha/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"nuevaPass123\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void cambiarContrasenhaSinSesion_ok() throws Exception {
        when(clienteService.getClientePorUsuario("usuarioTest")).thenReturn(cliente);
        when(clienteService.getClientePorEmail("test@email.com")).thenReturn(cliente);
        when(clienteService.cambiarContrasenhaSinSesion(eq("usuarioTest"), eq("test@email.com"), anyString())).thenReturn(cliente);
        doNothing().when(clienteService).enviarCorreoCambioContrasenha(any());

        mockMvc.perform(put("/api/cliente/cambiarContrasenha/usuarioTest/test@email.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"nuevaPass123\""))
                .andExpect(status().isOk());
    }

    @Test
    void cambiarContrasenhaSinSesion_notFound() throws Exception {
        when(clienteService.getClientePorUsuario("usuarioTest")).thenReturn(null);
        doNothing().when(clienteService).enviarCorreoCambioContrasenha(any());

        mockMvc.perform(put("/api/cliente/cambiarContrasenha/usuarioTest/test@email.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"nuevaPass123\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_ok() throws Exception {
        doNothing().when(clienteService).delete(1L);
        mockMvc.perform(delete("/api/cliente/delete/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void create_ok() throws Exception {
        when(clienteService.save(any())).thenReturn(cliente);
        doNothing().when(clienteService).enviarCorreoRegistro(any());

        mockMvc.perform(post("/api/cliente/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre": "Nombre Test",
                                    "apellidos": "Apellidos Test",
                                    "usuario": "usuarioTest",
                                    "email": "test@email.com",
                                    "contrasenha": "12345"
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void update_ok() throws Exception {
        when(clienteService.getClientePorId(1L)).thenReturn(cliente);
        when(clienteService.update(any())).thenReturn(cliente);

        mockMvc.perform(put("/api/cliente/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre": "Nombre Actualizado",
                                    "apellidos": "Apellidos Test",
                                    "usuario": "usuarioTest",
                                    "email": "test@email.com",
                                    "contrasenha": "12345"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void update_notFound() throws Exception {
        when(clienteService.getClientePorId(1L)).thenReturn(null);

        mockMvc.perform(put("/api/cliente/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nombre": "Nombre Actualizado",
                                    "apellidos": "Apellidos Test",
                                    "usuario": "usuarioTest",
                                    "email": "test@email.com",
                                    "contrasenha": "12345"
                                }
                                """))
                .andExpect(status().isNotFound());
    }
}