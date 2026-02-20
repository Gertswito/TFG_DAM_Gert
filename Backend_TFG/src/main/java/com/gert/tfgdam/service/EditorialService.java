package com.gert.tfgdam.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Editorial;
import com.gert.tfgdam.repository.EditorialRepository;

@Service
public class EditorialService {
    private final EditorialRepository editorialRepository;

    public EditorialService(EditorialRepository editorialRepository) {
        this.editorialRepository = editorialRepository;
    }

    public List<Editorial> getAllEditorial() {
        return editorialRepository.findAll();
    }

    public Editorial getEditorialPorId(Long id) {
        return editorialRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la editorial"));
    }

    public void delete(Long id) {
        if (!editorialRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado la editorial");
        }
        try {
            editorialRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esta editorial no puede ser eliminada", e);
        }
    }

    public Editorial save(Editorial editorial) {
        if (editorialRepository.existsByNombre(editorial.getNombre())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de editorial ya está registrado");
        }
        return editorialRepository.save(editorial);
    }

    public Editorial update(Editorial editorial) {
        Editorial existente = editorialRepository.findByNombre(editorial.getNombre());
        if (existente != null && !existente.getId().equals(editorial.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de editorial ya está registrado");
        }
        return editorialRepository.save(editorial);
    }
}
