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
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.FinalizarCompra;
import com.gert.tfgdam.entity.Libro;
import com.gert.tfgdam.entity.LineaVenta;
import com.gert.tfgdam.entity.Venta;
import com.gert.tfgdam.repository.LibroRepository;
import com.gert.tfgdam.repository.LineaVentaRepository;
import com.gert.tfgdam.repository.VentaRepository;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock
    private LineaVentaRepository lineaVentaRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private VentaService ventaService;

    private Venta venta;
    private Libro libro;
    private LineaVenta lineaVenta;
    private FinalizarCompra finalizarCompra;

    @BeforeEach
    void setUp() {
        libro = new Libro();
        libro.setId(1);
        libro.setTitulo("Libro Test");
        libro.setStock(10);

        venta = new Venta();
        venta.setId(1);
        venta.setPrecioFinal(BigDecimal.ZERO);

        lineaVenta = new LineaVenta();
        lineaVenta.setId(1);
        lineaVenta.setLibro(libro);
        lineaVenta.setCantidad(2);
        lineaVenta.setPrecioParcial(BigDecimal.valueOf(10));
        lineaVenta.setPrecioTotal(BigDecimal.valueOf(20));

        finalizarCompra = new FinalizarCompra();
        finalizarCompra.setVenta(venta);
        finalizarCompra.setLineasVenta(List.of(lineaVenta));
    }

    @Test
    void getAllVenta_ok() {
        when(ventaRepository.findAll()).thenReturn(List.of(venta));

        List<Venta> result = ventaService.getAllVenta();

        assertEquals(1, result.size());
        verify(ventaRepository).findAll();
    }

    @Test
    void getAllVentaPorUsuario_ok() {
        when(ventaRepository.findByClienteUsuario("usuario1")).thenReturn(List.of(venta));

        List<Venta> result = ventaService.getAllVentaPorUsuario("usuario1");

        assertEquals(1, result.size());
        verify(ventaRepository).findByClienteUsuario("usuario1");
    }

    @Test
    void getVentaPorId_ok() {
        when(ventaRepository.findById(1L)).thenReturn(Optional.of(venta));

        Venta result = ventaService.getVentaPorId(1L);

        assertEquals(1, result.getId());
    }

    @Test
    void getVentaPorId_notFound() {
        when(ventaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> ventaService.getVentaPorId(1L));
    }

    @Test
    void getAllVentaPorBusqueda_ok() {
        when(ventaRepository.findAllPorBusqueda("test")).thenReturn(List.of(venta));

        List<Venta> result = ventaService.getAllVentaPorBusqueda("test");

        assertEquals(1, result.size());
        verify(ventaRepository).findAllPorBusqueda("test");
    }

    @Test
    void save_ok() {
        when(ventaRepository.save(venta)).thenReturn(venta);

        Venta result = ventaService.save(venta);

        assertEquals(venta, result);
        verify(ventaRepository).save(venta);
    }

    @Test
    void finalizarCompra_ok() {
        when(ventaRepository.save(any(Venta.class))).thenReturn(venta);
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        when(libroRepository.save(any(Libro.class))).thenReturn(libro);
        when(lineaVentaRepository.save(any(LineaVenta.class))).thenReturn(lineaVenta);

        Venta result = ventaService.finalizarCompra(finalizarCompra);

        assertEquals(BigDecimal.valueOf(20), result.getPrecioFinal());
        assertEquals(8, libro.getStock());
        verify(ventaRepository, atLeast(2)).save(any(Venta.class));
        verify(lineaVentaRepository).save(lineaVenta);
        verify(libroRepository).save(libro);
    }

    @Test
    void finalizarCompra_stockInsuficiente() {
        libro.setStock(1);
        lenient().when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));
        assertThrows(RuntimeException.class, () -> ventaService.finalizarCompra(finalizarCompra));
    }

    @Test
    void enviarCorreoVenta_ok() {
        doNothing().when(emailService).enviarCorreoVenta(finalizarCompra);

        ventaService.enviarCorreoVenta(finalizarCompra);

        verify(emailService).enviarCorreoVenta(finalizarCompra);
    }
}
