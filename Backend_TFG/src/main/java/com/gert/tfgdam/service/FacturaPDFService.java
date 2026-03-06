package com.gert.tfgdam.service;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import com.gert.tfgdam.entity.FinalizarCompra;


@Service
public class FacturaPDFService {

    private final SpringTemplateEngine templateEngine;

    public FacturaPDFService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public byte[] generarFacturaPDF(FinalizarCompra finalizarCompra) {
        if (finalizarCompra.getVenta() == null) {
            throw new IllegalArgumentException("Venta no encontrada con ID: " + finalizarCompra.getVenta().getId());
        }

        if (finalizarCompra.getLineasVenta() == null || finalizarCompra.getLineasVenta().isEmpty()) {
            throw new IllegalArgumentException("No hay líneas de venta asociadas a la venta con ID: " +  finalizarCompra.getVenta().getId());
        }

        DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = finalizarCompra.getVenta().getFecha().format(formatterFecha);

        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm");
        String horaFormateada = finalizarCompra.getVenta().getHora().format(formatterHora);

        Context context = new Context();
        context.setVariable("ventaId", finalizarCompra.getVenta().getId());
        context.setVariable("clienteNombre", finalizarCompra.getVenta().getCliente().getNombre() + " " + finalizarCompra.getVenta().getCliente().getApellidos());
        context.setVariable("clienteUsuario", finalizarCompra.getVenta().getCliente().getUsuario());
        context.setVariable("fecha", fechaFormateada);
        context.setVariable("hora", horaFormateada);
        context.setVariable("precioFinal", finalizarCompra.getVenta().getPrecioFinal());
        context.setVariable("direccion1", finalizarCompra.getVenta().getDireccion().getCalle() + " " + finalizarCompra.getVenta().getDireccion().getNumero() + " " + finalizarCompra.getVenta().getDireccion().getPiso());
        context.setVariable("direccion2", finalizarCompra.getVenta().getDireccion().getCiudad() + ", " + finalizarCompra.getVenta().getDireccion().getProvincia() + " | " + finalizarCompra.getVenta().getDireccion().getCodigoPostal());

        List<Map<String, Object>> librosComprados = finalizarCompra.getLineasVenta().stream().map(linea -> {
            Map<String, Object> map = new HashMap<>();
            map.put("titulo", linea.getLibro().getTitulo());
            map.put("autor", linea.getLibro().getAutor().getNombre());
            map.put("editorial", linea.getLibro().getEditorial().getNombre());
            map.put("cantidadPedida", linea.getCantidad());
            map.put("precioParcial", linea.getPrecioParcial());
            map.put("precioTotal", linea.getPrecioTotal());
            return map;
        }).toList();
        context.setVariable("librosComprados", librosComprados);

        String html = templateEngine.process("factura", context);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el PDF", e);
        }
    }
}
