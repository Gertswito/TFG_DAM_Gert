package com.gert.tfgdam.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.TipoLibro;
import com.gert.tfgdam.repository.TipoLibroRepository;

@Service
public class TipoLibroService {
    private final TipoLibroRepository tipoLibroRepository;

    public TipoLibroService(TipoLibroRepository tipoLibroRepository) {
        this.tipoLibroRepository = tipoLibroRepository;
    }

    public List<TipoLibro> getAllTipoLibro() {
        return tipoLibroRepository.findAll();
    }

    public TipoLibro getTipoLibroPorId(Long id) {
        return tipoLibroRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el tipo de libro"));
    }

    public List<TipoLibro> getAllTipoLibroPorBusqueda(String texto) {
        return tipoLibroRepository.findAllPorBusqueda(texto);
    }

    public void delete(Long id) {
        if (!tipoLibroRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el tipo de libro");
        }
        try {
            tipoLibroRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este tipo de libro no puede ser borrado ya que tiene libros asociados", e);
        }
    }

    public TipoLibro save(TipoLibro tipoLibro) {
        if (tipoLibroRepository.existsByNombre(tipoLibro.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del tipo de libro ya está registrado");
        }
        return tipoLibroRepository.save(tipoLibro);
    }

    public TipoLibro update(TipoLibro tipoLibro) {
        TipoLibro existente = tipoLibroRepository.findByNombre(tipoLibro.getNombre());
        if (existente != null && !existente.getId().equals(tipoLibro.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre del tipo de libro ya está registrado");
        }
        return tipoLibroRepository.save(tipoLibro);
    }
}
