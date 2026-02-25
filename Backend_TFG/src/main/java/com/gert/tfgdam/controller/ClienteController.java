package com.gert.tfgdam.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Cliente;
import com.gert.tfgdam.service.ClienteService;
import com.gert.tfgdam.service.EmailService;

import jakarta.persistence.EntityNotFoundException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/cliente")
public class ClienteController {
    private final ClienteService clienteService;

    private final EmailService emailService;

    public ClienteController(ClienteService clienteService, EmailService emailService) {
        this.clienteService = clienteService;
        this.emailService = emailService;
    }

    @GetMapping("/get")
    public List<Cliente> getAllCliente() {
        return clienteService.getAllCliente();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Cliente> getClientePorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(clienteService.getClientePorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get/usuario/{usuario}")
    public ResponseEntity<Cliente> getClientePorUsuario(@PathVariable String usuario) {
        try {
            return ResponseEntity.ok(clienteService.getClientePorUsuario(usuario));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Cliente cliente) {
        String token = clienteService.login(cliente);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cambiarContrasenha/{id}")
    public ResponseEntity<Cliente> cambiarContrasenha(@PathVariable Long id, @RequestBody String contrasenha) {
        try {
            if (clienteService.getClientePorId(id) != null) {
                String contrasenhaSinComillas = contrasenha.replace("\"", "");
                clienteService.cambiarContrasenha(id, contrasenhaSinComillas);
                return ResponseEntity.ok(clienteService.getClientePorId(id));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            clienteService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }   

    @PostMapping("/new")
    public ResponseEntity<Object> create(@RequestBody Cliente cliente) throws URISyntaxException {
        try {
            Cliente nuevoCliente = clienteService.save(cliente);

            String asunto = "Bienvenido a Librerías Gert";
            String nombre = nuevoCliente.getNombre() + " " + nuevoCliente.getApellidos();
            String cuerpo = String.format("Hola %s,\n\nGracias por registrarte en Librerías Gert. Tu cuenta ha sido creada correctamente y ya puede iniciar sesión.\n\nAtentamente, el equipo de Librerías Gert", nombre);

            emailService.enviarCorreo(nuevoCliente.getEmail(), asunto, cuerpo);

            URI location = new URI("/new/" + nuevoCliente.getId());
            return ResponseEntity.created(location).body(nuevoCliente);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Cliente> update(@PathVariable Long id, @RequestBody Cliente cliente) {
        try {
            if (clienteService.getClientePorId(id) != null) {
                clienteService.update(cliente);
                return ResponseEntity.ok(cliente);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}