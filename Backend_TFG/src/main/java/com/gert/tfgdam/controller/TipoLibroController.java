package com.gert.tfgdam.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.TipoLibro;
import com.gert.tfgdam.service.TipoLibroService;

import jakarta.persistence.EntityNotFoundException;

import java.net.URI;
import java.net.URISyntaxException;
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
@RequestMapping("/api/tipo-libro")
public class TipoLibroController {
    private final TipoLibroService tipoLibroService;

    public TipoLibroController(TipoLibroService tipoLibroService) {
        this.tipoLibroService = tipoLibroService;
    }

    @GetMapping("/get")
    public List<TipoLibro> getAllTipoLibro() {
        return tipoLibroService.getAllTipoLibro();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<TipoLibro> getTipoLibroPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(tipoLibroService.getTipoLibroPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            tipoLibroService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }   

    @PostMapping("/new")
    public ResponseEntity<Object> create(@RequestBody TipoLibro tipoLibro) throws URISyntaxException {
        try {
            TipoLibro nuevaTipoLibro = tipoLibroService.save(tipoLibro);
            URI location = new URI("/new/" + nuevaTipoLibro.getId());
            return ResponseEntity.created(location).body(nuevaTipoLibro);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<TipoLibro> update(@PathVariable Long id, @RequestBody TipoLibro tipoLibro) {
        try {
            if (tipoLibroService.getTipoLibroPorId(id) != null) {
                tipoLibroService.update(tipoLibro);
                return ResponseEntity.ok(tipoLibro);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
