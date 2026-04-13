package com.gert.tfgdam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Cliente;
import com.gert.tfgdam.entity.FinalizarCompra;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private FacturaPDFService facturaPDFService;
    
    @Async
    public void enviarCorreoRegistro(Cliente nuevoCliente) {
        SimpleMailMessage mensaje = new SimpleMailMessage();

        String asunto = "Bienvenido a Librerías Gert";
        String nombre = nuevoCliente.getNombre() + " " + nuevoCliente.getApellidos();
        String cuerpo = String.format("Hola %s,\n\nGracias por registrarte en Librerías Gert. Tu cuenta ha sido creada correctamente y ya puede iniciar sesión.\n\nAtentamente, el equipo de Librerías Gert", nombre);

        mensaje.setTo(nuevoCliente.getEmail());
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        mensaje.setFrom("noreply.libreriasgert@gmail.com");

        mailSender.send(mensaje);
    }

    @Async
    public void enviarCorreoCambioContrasenha(Cliente cliente) {
        SimpleMailMessage mensaje = new SimpleMailMessage();

        String asunto = "Se ha cambiado la contraseña de su cuenta";
        String nombre = cliente.getNombre() + " " + cliente.getApellidos();
        String cuerpo = String.format("Hola %s,\n\nLe queríamos avisar de que se ha producido un cambio de la contraseña en su cuenta. Si no ha sido usted, le rogamos que por favor se ponga en contacto con los administradores de la apliación para solucionar este problema.\n\nAtentamente, el equipo de Librerías Gert", nombre);

        mensaje.setTo(cliente.getEmail());
        mensaje.setSubject(asunto);
        mensaje.setText(cuerpo);
        mensaje.setFrom("noreply.libreriasgert@gmail.com");

        mailSender.send(mensaje);
    }

    @Async
    public void enviarCorreoVenta(FinalizarCompra finalizarCompra) {
        if (finalizarCompra.getLineasVenta().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ventaSinLineas");
        }

        try {
            byte[] pdfBytes = facturaPDFService.generarFacturaPDF(finalizarCompra);

            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

            String asunto = "Factura de su compra #" + finalizarCompra.getVenta().getId();
            String nombre = finalizarCompra.getVenta().getCliente().getNombre() + " " + finalizarCompra.getVenta().getCliente().getApellidos();
            String cuerpo = String.format("Hola %s,\n\nSu compra ha sido realizada con éxito. Muchas gracias por confiar en nuestra tienda, le hemos adjuntado la factura de su pedido.\n\nAtentamente, el equipo de Librerías Gert", nombre);

            helper.setTo(finalizarCompra.getVenta().getCliente().getEmail());
            helper.setSubject(asunto);
            helper.setText(cuerpo);
            helper.setFrom("noreply.libreriasgert@gmail.com");

            ByteArrayResource recurso = new ByteArrayResource(pdfBytes);
            helper.addAttachment(("Factura_LibreriasGert_Numero" + finalizarCompra.getVenta().getId() + ".pdf"), recurso);

            mailSender.send(mensaje);
        } catch(MessagingException e) {
            throw new RuntimeException("Error al enviar el correo", e);
        }
    }
}
