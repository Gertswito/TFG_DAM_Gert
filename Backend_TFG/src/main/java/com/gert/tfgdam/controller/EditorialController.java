package com.gert.tfgdam.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Editorial;
import com.gert.tfgdam.service.EditorialService;

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
@RequestMapping("/api/editorial")
public class EditorialController {
    private final EditorialService editorialService;

    public EditorialController(EditorialService editorialService) {
        this.editorialService = editorialService;
    }

    @GetMapping("/get")
    public List<Editorial> getAllEditorial() {
        return editorialService.getAllEditorial();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Editorial> getEditorialPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(editorialService.getEditorialPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            editorialService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }   

    @PostMapping("/new")
    public ResponseEntity<Object> create(@RequestBody Editorial editorial) throws URISyntaxException {
        try {
            Editorial nuevaEditorial = editorialService.save(editorial);
            URI location = new URI("/new/" + nuevaEditorial.getId());
            return ResponseEntity.created(location).body(nuevaEditorial);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Editorial> update(@PathVariable Long id, @RequestBody Editorial editorial) {
        try {
            if (editorialService.getEditorialPorId(id) != null) {
                editorialService.update(editorial);
                return ResponseEntity.ok(editorial);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
