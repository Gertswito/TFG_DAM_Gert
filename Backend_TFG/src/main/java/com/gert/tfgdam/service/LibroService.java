package com.gert.tfgdam.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Libro;
import com.gert.tfgdam.repository.LibroRepository;

@Service
public class LibroService {
    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public List<Libro> getAllLibro() {
        return libroRepository.findAll();
    }

    public List<Libro> getAllLibroPorTipo(String tipoLibro) {
        List<Libro> libros = libroRepository.findByTipoLibro_Nombre(tipoLibro);

        if (libros.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existen libros para el tipo: " + tipoLibro);
        }
        return libros;
    }

    public List<Libro> getAllLibroPorTipoGenero(String tipoLibro, String genero) {
        List<Libro> libros = libroRepository.findByTipoLibro_NombreAndGeneros_Nombre(tipoLibro, genero);

        if (libros.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existen libros para el tipo o géneros: " + tipoLibro + " | " + genero);
        }
        return libros;
    }

    public Libro getLibroPorId(Long id) {
        return libroRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el libro"));
    }

    public void delete(Long id) {
        if (!libroRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el libro");
        }
        try {
            libroRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este libro no puede ser eliminado", e);
        }
    }

    public Libro save(Libro libro) {
        if (libroRepository.existsByTitulo(libro.getTitulo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del libro ya está registrado");
        }
        return libroRepository.save(libro);
    }

    public Libro update(Libro libro) {
        Libro existente = libroRepository.findByTitulo(libro.getTitulo());
        if (existente != null && !existente.getId().equals(libro.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del libro ya está registrado");
        }
        return libroRepository.save(libro);
    }
}

