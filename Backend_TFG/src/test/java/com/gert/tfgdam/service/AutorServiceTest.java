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

import com.gert.tfgdam.entity.Autor;
import com.gert.tfgdam.repository.AutorRepository;

@ExtendWith(MockitoExtension.class)
class AutorServiceTest {

    @Mock
    private AutorRepository autorRepository;

    @InjectMocks
    private AutorService autorService;

    private Autor autor;

    @BeforeEach
    void inicializarEditorial() {
        autor = new Autor();
        autor.setId(1);
        autor.setNombre("Autor Test");
    }

    @Test
    void getAllAutor_ok() {
        when(autorRepository.findAll()).thenReturn(List.of(autor));
        List<Autor> result = autorService.getAllAutor();
        assertEquals(1, result.size());
        verify(autorRepository).findAll();
    }

    @Test
    void getAutorPorId_ok() {
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));
        Autor result = autorService.getAutorPorId(1L);
        assertEquals("Autor Test", result.getNombre());
    }

    @Test
    void getAutorPorId_notFound() {
        when(autorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> {
            autorService.getAutorPorId(1L);
        });
    }

    @Test
    void delete_ok() {
        when(autorRepository.existsById(1L)).thenReturn(true);
        autorService.delete(1L);
        verify(autorRepository).deleteById(1L);
    }

    @Test
    void delete_notFound() {
        when(autorRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> {
            autorService.delete(1L);
        });
    }

    @Test
    void save_ok() {
        when(autorRepository.existsByNombre("Autor Test")).thenReturn(false);
        when(autorRepository.save(autor)).thenReturn(autor);

        Autor result = autorService.save(autor);
        assertEquals("Autor Test", result.getNombre());
    }

    @Test
    void save_nombreDuplicado() {
        when(autorRepository.existsByNombre("Autor Test")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> {
            autorService.save(autor);
        });
    }

    @Test
    void update_ok() {
        when(autorRepository.findByNombre("Autor Test")).thenReturn(autor);
        when(autorRepository.save(autor)).thenReturn(autor);

        Autor result = autorService.update(autor);
        assertNotNull(result);
    }

    @Test
    void update_nombreDuplicado() {
        Autor otro = new Autor();
        otro.setId(2);
        otro.setNombre("Autor Test");

        when(autorRepository.findByNombre("Autor Test")).thenReturn(otro);
        assertThrows(ResponseStatusException.class, () -> {
            autorService.update(autor);
        });
    }
}
