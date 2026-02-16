package com.gert.tfgdam.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Genero;
import com.gert.tfgdam.repository.GeneroRepository;

@Service
public class GeneroService {
    private final GeneroRepository generoRepository;

    public GeneroService(GeneroRepository generoRepository) {
        this.generoRepository = generoRepository;
    }

    public List<Genero> getAllGenero() {
        return generoRepository.findAll();
    }

    public Genero getGeneroPorId(Long id) {
        return generoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "generoNoExiste"));
    }

    public void delete(Long id) {
        if (!generoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "generoNoExiste");
        }
        try {
            generoRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "generoNoSePuedeEliminar", e);
        }
    }

    public Genero save(Genero genero) {
        if (generoRepository.existsByNombre(genero.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombreExiste");
        }
        return generoRepository.save(genero);
    }

    public Genero update(Genero genero) {
        Genero existente = generoRepository.findByNombre(genero.getNombre());
        if (existente != null && !existente.getId().equals(genero.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombreExiste");
        }
        return generoRepository.save(genero);
    }
}
