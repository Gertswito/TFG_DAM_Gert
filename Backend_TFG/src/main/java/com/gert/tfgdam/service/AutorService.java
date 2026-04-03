package com.gert.tfgdam.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Autor;
import com.gert.tfgdam.repository.AutorRepository;

@Service
public class AutorService {
    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    public List<Autor> getAllAutor() {
        return autorRepository.findAll();
    }

    public Autor getAutorPorId(Long id) {
        return autorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado al autor"));
    }

    public List<Autor> getAllAutorPorBusqueda(String texto) {
        return autorRepository.findAllPorBusqueda(texto);
    }

    public void delete(Long id) {
        if (!autorRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado al autor");
        }
        try {
            autorRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este autor no puede ser eliminado", e);
        }
    }

    public Autor save(Autor autor) {
        if (autorRepository.existsByNombre(autor.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de autor ya está registrado");
        }
        return autorRepository.save(autor);
    }

    public Autor update(Autor autor) {
        Autor existente = autorRepository.findByNombre(autor.getNombre());
        if (existente != null && !existente.getId().equals(autor.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de autor ya está registrado");
        }
        return autorRepository.save(autor);
    }
}
