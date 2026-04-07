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

import com.gert.tfgdam.entity.Genero;
import com.gert.tfgdam.repository.GeneroRepository;

@ExtendWith(MockitoExtension.class)
class GeneroServiceTest {

    @Mock
    private GeneroRepository generoRepository;

    @InjectMocks
    private GeneroService generoService;

    private Genero genero;

    @BeforeEach
    void inicializarEditorial() {
        genero = new Genero();
        genero.setId(1);
        genero.setNombre("Genero Test");
    }

    @Test
    void getAllGenero_ok() {
        when(generoRepository.findAll()).thenReturn(List.of(genero));
        List<Genero> result = generoService.getAllGenero();
        assertEquals(1, result.size());
        verify(generoRepository).findAll();
    }

    @Test
    void getGeneroPorId_ok() {
        when(generoRepository.findById(1L)).thenReturn(Optional.of(genero));
        Genero result = generoService.getGeneroPorId(1L);
        assertEquals("Genero Test", result.getNombre());
    }

    @Test
    void getGeneroPorId_notFound() {
        when(generoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> {
            generoService.getGeneroPorId(1L);
        });
    }

    @Test
    void delete_ok() {
        when(generoRepository.existsById(1L)).thenReturn(true);
        generoService.delete(1L);
        verify(generoRepository).deleteById(1L);
    }

    @Test
    void delete_notFound() {
        when(generoRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> {
            generoService.delete(1L);
        });
    }

    @Test
    void save_ok() {
        when(generoRepository.existsByNombre("Genero Test")).thenReturn(false);
        when(generoRepository.save(genero)).thenReturn(genero);

        Genero result = generoService.save(genero);
        assertEquals("Genero Test", result.getNombre());
    }

    @Test
    void save_nombreDuplicado() {
        when(generoRepository.existsByNombre("Genero Test")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> {
            generoService.save(genero);
        });
    }

    @Test
    void update_ok() {
        when(generoRepository.findByNombre("Genero Test")).thenReturn(genero);
        when(generoRepository.save(genero)).thenReturn(genero);

        Genero result = generoService.update(genero);
        assertNotNull(result);
    }

    @Test
    void update_nombreDuplicado() {
        Genero otro = new Genero();
        otro.setId(2);
        otro.setNombre("Genero Test");

        when(generoRepository.findByNombre("Genero Test")).thenReturn(otro);
        assertThrows(ResponseStatusException.class, () -> {
            generoService.update(genero);
        });
    }
}
