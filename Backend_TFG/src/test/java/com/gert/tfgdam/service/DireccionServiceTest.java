package com.gert.tfgdam.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Cliente;
import com.gert.tfgdam.entity.Direccion;
import com.gert.tfgdam.repository.DireccionRepository;
import com.gert.tfgdam.repository.VentaRepository;

@ExtendWith(MockitoExtension.class)
class DireccionServiceTest {

    @Mock
    private DireccionRepository direccionRepository;

    @Mock
    private VentaRepository ventaRepository;

    @InjectMocks
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
    void getAllDireccion_ok() {
        when(direccionRepository.findAll()).thenReturn(List.of(direccion));
        List<Direccion> result = direccionService.getAllDireccion();
        assertEquals(1, result.size());
        verify(direccionRepository).findAll();
    }

    @Test
    void getAllDireccionPorClienteId_ok() {
        when(direccionRepository.findByClienteId(1L)).thenReturn(List.of(direccion));
        List<Direccion> result = direccionService.getAllDireccionPorClienteId(1L);
        assertEquals(1, result.size());
        verify(direccionRepository).findByClienteId(1L);
    }

    @Test
    void getDireccionPorId_ok() {
        when(direccionRepository.findById(1L)).thenReturn(Optional.of(direccion));
        Direccion result = direccionService.getDireccionPorId(1L);
        assertEquals("Calle Test", result.getCalle());
    }

    @Test
    void getDireccionPorId_notFound() {
        when(direccionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> direccionService.getDireccionPorId(1L));
    }

    @Test
    void delete_ok_sinVentas() {
        when(direccionRepository.existsById(1L)).thenReturn(true);
        when(ventaRepository.existsByDireccionId(1L)).thenReturn(false);
        doNothing().when(direccionRepository).deleteById(1L);

        direccionService.delete(1L);
        verify(direccionRepository).deleteById(1L);
    }

    @Test
    void delete_ok_conVentas() {
        when(direccionRepository.existsById(1L)).thenReturn(true);
        when(ventaRepository.existsByDireccionId(1L)).thenReturn(true);
        when(direccionRepository.findById(1L)).thenReturn(Optional.of(direccion));
        when(direccionRepository.save(any())).thenReturn(direccion);

        direccionService.delete(1L);
        assertFalse(direccion.getActivo());
        verify(direccionRepository).save(direccion);
    }

    @Test
    void delete_notFound() {
        when(direccionRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> direccionService.delete(1L));
    }

    @Test
    void save_ok() {
        when(direccionRepository.save(direccion)).thenReturn(direccion);
        Direccion result = direccionService.save(direccion);
        assertEquals("Calle Test", result.getCalle());
        verify(direccionRepository).save(direccion);
    }

    @Test
    void update_ok() {
        when(direccionRepository.save(direccion)).thenReturn(direccion);
        Direccion result = direccionService.update(direccion);
        assertEquals("Calle Test", result.getCalle());
        verify(direccionRepository).save(direccion);
    }
}
