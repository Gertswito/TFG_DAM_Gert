package com.gert.tfgdam.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Autor;
import com.gert.tfgdam.entity.Cliente;
import com.gert.tfgdam.entity.Editorial;
import com.gert.tfgdam.entity.Libro;
import com.gert.tfgdam.entity.TipoLibro;
import com.gert.tfgdam.repository.ClienteRepository;
import com.gert.tfgdam.repository.LibroRepository;

@ExtendWith(MockitoExtension.class)
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private LibroService libroService;

    private Libro libro;
    private Cliente cliente;
    private Autor autor;
    private Editorial editorial;
    private TipoLibro tipoLibro;

    @BeforeEach
    void init() {
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

        cliente = new Cliente();
        cliente.setId(1);
        cliente.setUsuario("usuarioTest");
        cliente.setLibrosDeseados(new HashSet<>());
    }

    @Test
    void getAllLibro_ok() {
        when(libroRepository.findAll()).thenReturn(List.of(libro));
        List<Libro> result = libroService.getAllLibro();
        assertEquals(1, result.size());
        verify(libroRepository).findAll();
    }

    @Test
    void getLibroPorId_ok() {
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        Libro result = libroService.getLibroPorId(1L);
        assertEquals("Libro Test", result.getTitulo());
    }

    @Test
    void getLibroPorId_notFound() {
        when(libroRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> libroService.getLibroPorId(1L));
    }

    @Test
    void delete_ok() {
        when(libroRepository.existsById(1L)).thenReturn(true);
        libroService.delete(1L);
        verify(libroRepository).deleteById(1L);
    }

    @Test
    void delete_notFound() {
        when(libroRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> libroService.delete(1L));
    }

    @Test
    void save_ok() {
        when(libroRepository.existsByTitulo("Libro Test")).thenReturn(false);
        when(libroRepository.save(libro)).thenReturn(libro);
        Libro result = libroService.save(libro);
        assertEquals("Libro Test", result.getTitulo());
    }

    @Test
    void save_tituloDuplicado() {
        when(libroRepository.existsByTitulo("Libro Test")).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> libroService.save(libro));
    }

    @Test
    void update_ok() {
        when(libroRepository.findByTitulo("Libro Test")).thenReturn(libro);
        when(libroRepository.save(libro)).thenReturn(libro);
        Libro result = libroService.update(libro);
        assertNotNull(result);
    }

    @Test
    void update_tituloDuplicado() {
        Libro otro = new Libro();
        otro.setId(2);
        otro.setTitulo("Libro Test");
        when(libroRepository.findByTitulo("Libro Test")).thenReturn(otro);
        assertThrows(ResponseStatusException.class, () -> libroService.update(libro));
    }

    @Test
    void addLibroListaDeseados_ok() {
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(clienteRepository.findByUsuario("usuarioTest")).thenReturn(cliente);
        libroService.addLibroListaDeseados(1L, cliente);
        assertTrue(cliente.getLibrosDeseados().contains(libro));
        verify(clienteRepository).save(cliente);
    }

    @Test
    void addLibroListaDeseados_usuarioNoEncontrado() {
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(clienteRepository.findByUsuario("usuarioTest")).thenReturn(null);
        assertThrows(ResponseStatusException.class, () -> libroService.addLibroListaDeseados(1L, cliente));
    }

    @Test
    void deleteLibroListaDeseados_ok() {
        cliente.getLibrosDeseados().add(libro);
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(clienteRepository.findByUsuario("usuarioTest")).thenReturn(cliente);
        libroService.deleteLibroListaDeseados(1L, "usuarioTest");
        assertFalse(cliente.getLibrosDeseados().contains(libro));
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deleteLibroListaDeseados_noEnLista() {
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(clienteRepository.findByUsuario("usuarioTest")).thenReturn(cliente);
        assertThrows(ResponseStatusException.class, () -> libroService.deleteLibroListaDeseados(1L, "usuarioTest"));
    }

    @Test
    void actualizarStock_ok() {
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(libroRepository.save(any())).thenReturn(libro);
        Libro result = libroService.actualizarStock(1L, 5);
        assertEquals(15, result.getStock());
    }

    @Test
    void getAllLibroLimitadoPorStock_ok() {
        when(libroRepository.findLibrosLimitadoPorStock()).thenReturn(List.of(libro));
        List<Libro> result = libroService.getAllLibroLimitadoPorStock();
        assertEquals(1, result.size());
    }

    @Test
    void getAllLibroLimitadoParaView_ok() {
        when(libroRepository.findLibrosLimitadosYDivididosPorTipoLibro()).thenReturn(List.of(libro));
        List<Libro> result = libroService.getAllLibroLimitadoParaView();
        assertEquals(1, result.size());
    }

    @Test
    void getAllLibroPorTipo_ok() {
        when(libroRepository.findByTipoLibro_Nombre("Tipo Test")).thenReturn(List.of(libro));
        List<Libro> result = libroService.getAllLibroPorTipo("Tipo Test");
        assertEquals(1, result.size());
    }

    @Test
    void getAllLibroPorTipoGenero_ok() {
        when(libroRepository.findByTipoLibro_NombreAndGeneros_Nombre("Tipo Test", "Genero Test")).thenReturn(List.of(libro));
        List<Libro> result = libroService.getAllLibroPorTipoGenero("Tipo Test", "Genero Test");
        assertEquals(1, result.size());
    }

    @Test
    void getAllPorNombreDeAutor_ok() {
        when(libroRepository.findByAutor_Nombre("Autor Test")).thenReturn(List.of(libro));
        List<Libro> result = libroService.getAllPorNombreDeAutor("Autor Test");
        assertEquals(1, result.size());
    }

    @Test
    void getAllPorNombreDeEditorial_ok() {
        when(libroRepository.findByEditorial_Nombre("Editorial Test")).thenReturn(List.of(libro));
        List<Libro> result = libroService.getAllPorNombreDeEditorial("Editorial Test");
        assertEquals(1, result.size());
    }

    @Test
    void getAllLibroNovedadesMes_ok() {
        when(libroRepository.findByMesActual(anyInt(), anyInt())).thenReturn(List.of(libro));
        List<Libro> result = libroService.getAllLibroNovedadesMes();
        assertEquals(1, result.size());
    }

    @Test
    void getAllLibroNovedadesUltimaAdicion_ok() {
        when(libroRepository.findTop10ExcluyendoMesActual(anyInt(), anyInt())).thenReturn(List.of(libro));
        List<Libro> result = libroService.getAllLibroNovedadesUltimaAdicion();
        assertEquals(1, result.size());
    }
}
