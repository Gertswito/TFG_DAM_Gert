package com.gert.tfgdam.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.TipoLibro;
import com.gert.tfgdam.repository.TipoLibroRepository;

@ExtendWith(MockitoExtension.class)
class TipoLibroServiceTest {

    @Mock
    private TipoLibroRepository tipoLibroRepository;

    @InjectMocks
    private TipoLibroService tipoLibroService;

    private TipoLibro tipoLibro;

    @BeforeEach
    void inicializarEditorial() {
        tipoLibro = new TipoLibro();
        tipoLibro.setId(1);
        tipoLibro.setNombre("TipoLibro Test");
    }

    @Test
    void getAllTipoLibro_ok() {
        when(tipoLibroRepository.findAll()).thenReturn(List.of(tipoLibro));
        List<TipoLibro> result = tipoLibroService.getAllTipoLibro();
        assertEquals(1, result.size());
        verify(tipoLibroRepository).findAll();
    }

    @Test
    void getTipoLibroPorId_ok() {
        when(tipoLibroRepository.findById(1L)).thenReturn(Optional.of(tipoLibro));
        TipoLibro result = tipoLibroService.getTipoLibroPorId(1L);
        assertEquals("TipoLibro Test", result.getNombre());
    }

    @Test
    void getTipoLibroPorId_notFound() {
        when(tipoLibroRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> {
            tipoLibroService.getTipoLibroPorId(1L);
        });
    }

    @Test
    void delete_ok() {
        when(tipoLibroRepository.existsById(1L)).thenReturn(true);
        tipoLibroService.delete(1L);
        verify(tipoLibroRepository).deleteById(1L);
    }

    @Test
    void delete_notFound() {
        when(tipoLibroRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> {
            tipoLibroService.delete(1L);
        });
    }

    @Test
    void save_ok() {
        when(tipoLibroRepository.existsByNombre("TipoLibro Test")).thenReturn(false);
        when(tipoLibroRepository.save(tipoLibro)).thenReturn(tipoLibro);

        TipoLibro result = tipoLibroService.save(tipoLibro);
        assertEquals("TipoLibro Test", result.getNombre());
    }

    @Test
    void save_nombreDuplicado() {
        when(tipoLibroRepository.existsByNombre("TipoLibro Test")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> {
            tipoLibroService.save(tipoLibro);
        });
    }

    @Test
    void update_ok() {
        when(tipoLibroRepository.findByNombre("TipoLibro Test")).thenReturn(tipoLibro);
        when(tipoLibroRepository.save(tipoLibro)).thenReturn(tipoLibro);

        TipoLibro result = tipoLibroService.update(tipoLibro);
        assertNotNull(result);
    }

    @Test
    void update_nombreDuplicado() {
        TipoLibro otro = new TipoLibro();
        otro.setId(2);
        otro.setNombre("TipoLibro Test");

        when(tipoLibroRepository.findByNombre("TipoLibro Test")).thenReturn(otro);
        assertThrows(ResponseStatusException.class, () -> {
            tipoLibroService.update(tipoLibro);
        });
    }
}
