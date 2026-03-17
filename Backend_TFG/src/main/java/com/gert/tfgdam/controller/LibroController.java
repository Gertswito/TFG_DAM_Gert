package com.gert.tfgdam.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Cliente;
import com.gert.tfgdam.entity.Libro;
import com.gert.tfgdam.service.LibroService;

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
@RequestMapping("/api/libro")
public class LibroController {
    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping("/get")
    public List<Libro> getAllLibro() {
        return libroService.getAllLibro();
    }

    @GetMapping("/get/stock")
    public List<Libro> getAllLibroLimitadoPorStock() {
        return libroService.getAllLibroLimitadoPorStock();
    }

    @GetMapping("/get/view")
    public List<Libro> getAllLibroLimitadoParaView() {
        return libroService.getAllLibroLimitadoParaView();
    }

    @GetMapping("/get/tipo/{tipo-libro}")
    public List<Libro> getAllLibroPorTipo(@PathVariable("tipo-libro") String tipoLibro) {
        return libroService.getAllLibroPorTipo(tipoLibro);
    }

    @GetMapping("/get/tipo/{tipo-libro}/genero/{genero}")
    public List<Libro> getAllLibroPorTipoGenero(@PathVariable("tipo-libro") String tipoLibro, @PathVariable("genero") String genero) {
        return libroService.getAllLibroPorTipoGenero(tipoLibro, genero);
    }

    @GetMapping("/get/autor/{autor}")
    public List<Libro> getAllPorNombreDeAutor(@PathVariable("autor") String autor) {
        return libroService.getAllPorNombreDeAutor(autor);
    }

    @GetMapping("/get/editorial/{editorial}")
    public List<Libro> getAllPorNombreDeEditorial(@PathVariable("editorial") String editorial) {
        return libroService.getAllPorNombreDeEditorial(editorial);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Libro> getLibroPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(libroService.getLibroPorId(id));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/get/lista-deseados/{usuario}")
    public List<Libro> getAllLibrosEnListaDeseados(@PathVariable String usuario) {
        return libroService.getAllLibrosEnListaDeseados(usuario);
    }

    @GetMapping("/get/lista-deseados/{id}/{usuario}")
    public ResponseEntity<Libro> getLibroEnListaDeseados(@PathVariable Integer id, @PathVariable String usuario) {
        Libro libro = libroService.getLibroEnListaDeseados(id, usuario);
        return ResponseEntity.ok(libro);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            libroService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }   

    @PostMapping("/new")
    public ResponseEntity<Object> create(@RequestBody Libro libro) throws URISyntaxException {
        try {
            Libro nuevaLibro = libroService.save(libro);
            URI location = new URI("/new/" + nuevaLibro.getId());
            return ResponseEntity.created(location).body(nuevaLibro);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(Map.of("error", ex.getReason()));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Libro> update(@PathVariable Long id, @RequestBody Libro libro) {
        try {
            if (libroService.getLibroPorId(id) != null) {
                libroService.update(libro);
                return ResponseEntity.ok(libro);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/lista-deseados/add/{id}")
    public ResponseEntity<Void>addLibroListaDeseados(@PathVariable Long id, @RequestBody Cliente cliente) {
        libroService.addLibroListaDeseados(id, cliente);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/lista-deseados/delete/{id}/{usuario}")
    public ResponseEntity<Void>deleteLibroListaDeseados(@PathVariable Long id, @PathVariable String usuario) {
        libroService.deleteLibroListaDeseados(id, usuario);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/stock/{id}")
    public ResponseEntity<Libro> actualizarStock(@PathVariable Long id, @RequestBody Integer addedStock) {
        try {
            if (libroService.getLibroPorId(id) != null) {
                libroService.actualizarStock(id, addedStock);
                return ResponseEntity.ok(libroService.getLibroPorId(id));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}