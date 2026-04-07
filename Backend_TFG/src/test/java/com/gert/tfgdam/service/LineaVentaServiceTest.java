package com.gert.tfgdam.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Libro;
import com.gert.tfgdam.entity.LineaVenta;
import com.gert.tfgdam.entity.Venta;
import com.gert.tfgdam.repository.LibroRepository;
import com.gert.tfgdam.repository.LineaVentaRepository;
import com.gert.tfgdam.repository.VentaRepository;

@ExtendWith(MockitoExtension.class)
class LineaVentaServiceTest {

    @Mock
    private LineaVentaRepository lineaVentaRepository;

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LineaVentaService lineaVentaService;

    private LineaVenta lineaVenta;
    private Libro libro;
    private Venta venta;

    @BeforeEach
    void inicializarDatos() {
        libro = new Libro();
        libro.setId(1);
        libro.setTitulo("Libro Test");
        libro.setStock(10);
        libro.setPrecio(BigDecimal.valueOf(10));

        venta = new Venta();
        venta.setId(1);
        venta.setPrecioFinal(BigDecimal.ZERO);

        lineaVenta = new LineaVenta();
        lineaVenta.setId(1);
        lineaVenta.setLibro(libro);
        lineaVenta.setVenta(venta);
        lineaVenta.setCantidad(2);
        lineaVenta.setPrecioParcial(BigDecimal.valueOf(10));
        lineaVenta.setPrecioTotal(BigDecimal.valueOf(20));
    }

    @Test
    void getAllLineaVenta_ok() {
        when(lineaVentaRepository.findAll()).thenReturn(List.of(lineaVenta));

        List<LineaVenta> result = lineaVentaService.getAllLineaVenta();

        assertEquals(1, result.size());
        verify(lineaVentaRepository).findAll();
    }

    @Test
    void getLineaVentaPorId_ok() {
        when(lineaVentaRepository.findById(1L)).thenReturn(Optional.of(lineaVenta));

        LineaVenta result = lineaVentaService.getLineaVentaPorId(1L);

        assertEquals(1, result.getId());
    }

    @Test
    void getLineaVentaPorId_notFound() {
        when(lineaVentaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> lineaVentaService.getLineaVentaPorId(1L));
    }

    @Test
    void getAllLineaVentaPorVenta_ok() {
        when(lineaVentaRepository.findByVentaId(1L)).thenReturn(List.of(lineaVenta));

        List<LineaVenta> result = lineaVentaService.getAllLineaVentaPorVenta(1L);

        assertEquals(1, result.size());
        verify(lineaVentaRepository).findByVentaId(1L);
    }

    @Test
    void save_nuevaLinea_ok() {
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        when(lineaVentaRepository.findByVentaIdAndLibroId(1L, 1L)).thenReturn(Optional.empty());
        when(libroRepository.save(any())).thenReturn(libro);
        when(ventaRepository.save(any())).thenReturn(venta);
        when(lineaVentaRepository.save(any())).thenReturn(lineaVenta);

        LineaVenta result = lineaVentaService.save(lineaVenta);

        assertEquals(2, result.getCantidad());
        assertEquals(BigDecimal.valueOf(20), result.getPrecioTotal());
        assertEquals(8, libro.getStock());
        verify(lineaVentaRepository).save(lineaVenta);
    }

    @Test
    void save_lineaExistente_ok() {
        LineaVenta existente = new LineaVenta();
        existente.setId(2);
        existente.setLibro(libro);
        existente.setVenta(venta);
        existente.setCantidad(1);
        existente.setPrecioParcial(BigDecimal.valueOf(10));
        existente.setPrecioTotal(BigDecimal.valueOf(10));

        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        when(lineaVentaRepository.findByVentaIdAndLibroId(1L, 1L)).thenReturn(Optional.of(existente));
        when(libroRepository.save(any())).thenReturn(libro);
        when(ventaRepository.save(any())).thenReturn(venta);
        when(lineaVentaRepository.save(any())).thenReturn(existente);

        LineaVenta result = lineaVentaService.save(lineaVenta);

        assertEquals(3, result.getCantidad());
        assertEquals(BigDecimal.valueOf(30), result.getPrecioTotal());
        assertEquals(8, libro.getStock());
    }

    @Test
    void save_libroNoEncontrado() {
        when(libroRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> lineaVentaService.save(lineaVenta));
    }

    @Test
    void save_ventaNoEncontrada() {
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(ventaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> lineaVentaService.save(lineaVenta));
    }

    @Test
    void save_stockInsuficiente() {
        libro.setStock(1); 
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));
        when(lineaVentaRepository.findByVentaIdAndLibroId(1L, 1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> lineaVentaService.save(lineaVenta));
    }
}
