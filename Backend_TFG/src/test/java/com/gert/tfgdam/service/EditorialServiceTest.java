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

import com.gert.tfgdam.entity.Editorial;
import com.gert.tfgdam.repository.EditorialRepository;

@ExtendWith(MockitoExtension.class)
class EditorialServiceTest {

    @Mock
    private EditorialRepository editorialRepository;

    @InjectMocks
    private EditorialService editorialService;

    private Editorial editorial;

    @BeforeEach
    void inicializarEditorial() {
        editorial = new Editorial();
        editorial.setId(1);
        editorial.setNombre("Editorial Test");
    }

    @Test
    void getAllEditorial_ok() {
        when(editorialRepository.findAll()).thenReturn(List.of(editorial));
        List<Editorial> result = editorialService.getAllEditorial();
        assertEquals(1, result.size());
        verify(editorialRepository).findAll();
    }

    @Test
    void getEditorialPorId_ok() {
        when(editorialRepository.findById(1L)).thenReturn(Optional.of(editorial));
        Editorial result = editorialService.getEditorialPorId(1L);
        assertEquals("Editorial Test", result.getNombre());
    }

    @Test
    void getEditorialPorId_notFound() {
        when(editorialRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> {
            editorialService.getEditorialPorId(1L);
        });
    }

    @Test
    void delete_ok() {
        when(editorialRepository.existsById(1L)).thenReturn(true);
        editorialService.delete(1L);
        verify(editorialRepository).deleteById(1L);
    }

    @Test
    void delete_notFound() {
        when(editorialRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> {
            editorialService.delete(1L);
        });
    }

    @Test
    void save_ok() {
        when(editorialRepository.existsByNombre("Editorial Test")).thenReturn(false);
        when(editorialRepository.save(editorial)).thenReturn(editorial);

        Editorial result = editorialService.save(editorial);
        assertEquals("Editorial Test", result.getNombre());
    }

    @Test
    void save_nombreDuplicado() {
        when(editorialRepository.existsByNombre("Editorial Test")).thenReturn(true);

        assertThrows(ResponseStatusException.class, () -> {
            editorialService.save(editorial);
        });
    }

    @Test
    void update_ok() {
        when(editorialRepository.findByNombre("Editorial Test")).thenReturn(editorial);
        when(editorialRepository.save(editorial)).thenReturn(editorial);

        Editorial result = editorialService.update(editorial);
        assertNotNull(result);
    }

    @Test
    void update_nombreDuplicado() {
        Editorial otro = new Editorial();
        otro.setId(2);
        otro.setNombre("Editorial Test");

        when(editorialRepository.findByNombre("Editorial Test")).thenReturn(otro);
        assertThrows(ResponseStatusException.class, () -> {
            editorialService.update(editorial);
        });
    }
}

