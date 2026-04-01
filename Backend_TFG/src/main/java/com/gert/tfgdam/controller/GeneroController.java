package com.gert.tfgdam.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Genero;
import com.gert.tfgdam.service.GeneroService;

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
@RequestMapping("/api/genero")
public class GeneroController {
    private final GeneroService generoService;

    public GeneroController(GeneroService generoService) {
        this.generoService = generoService;
    }

    @GetMapping("/get")
    public List<Genero> getAllGenero() {
        return generoService.getAllGenero();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Genero> getGeneroPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(generoService.getGeneroPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            generoService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }   

    @PostMapping("/new")
    public ResponseEntity<Object> create(@RequestBody Genero genero) throws URISyntaxException {
        try {
            Genero nuevaGenero = generoService.save(genero);
            URI location = new URI("/new/" + nuevaGenero.getId());
            return ResponseEntity.created(location).body(nuevaGenero);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Genero> update(@PathVariable Long id, @RequestBody Genero genero) {
        try {
            if (generoService.getGeneroPorId(id) != null) {
                generoService.update(genero);
                return ResponseEntity.ok(genero);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
