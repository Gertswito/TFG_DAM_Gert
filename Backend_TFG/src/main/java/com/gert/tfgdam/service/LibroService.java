package com.gert.tfgdam.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Cliente;
import com.gert.tfgdam.entity.Libro;
import com.gert.tfgdam.repository.ClienteRepository;
import com.gert.tfgdam.repository.LibroRepository;

@Service
public class LibroService {
    private final LibroRepository libroRepository;
    private final ClienteRepository clienteRepository;

    public LibroService(LibroRepository libroRepository, ClienteRepository clienteRepository) {
        this.libroRepository = libroRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Libro> getAllLibro() {
        return libroRepository.findAll();
    }

    public List<Libro> getAllLibroLimitadoPorStock() {
        return libroRepository.findLibrosLimitadoPorStock();
    }

    public List<Libro> getAllLibroLimitadoParaView() {
        return libroRepository.findLibrosLimitadosYDivididosPorTipoLibro();
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

    public List<Libro> getAllPorNombreDeAutor(String autor) {
        List<Libro> libros = libroRepository.findByAutor_Nombre(autor);

        if (libros.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existen libros para el autor seleccionado");
        }
        return libros;
    }

    public List<Libro> getAllPorNombreDeEditorial(String editorial) {
        List<Libro> libros = libroRepository.findByEditorial_Nombre(editorial);

        if (libros.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No existen libros para el autor seleccionado");
        }
        return libros;
    }

    public List<Libro> getAllLibroNovedadesMes() {
        int mes = LocalDate.now().getMonthValue();
        int yearEnIngles = LocalDate.now().getYear();
        return libroRepository.findByMesActual(mes, yearEnIngles);
    }

    public List<Libro> getAllLibroNovedadesUltimaAdicion() {
        int mes = LocalDate.now().getMonthValue();
        int yearEnIngles = LocalDate.now().getYear();
        return libroRepository.findTop10ExcluyendoMesActual(mes, yearEnIngles);
    }

    public Libro getLibroPorId(Long id) {
        return libroRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el libro"));
    }

    public List<Libro> getAllLibrosEnListaDeseados(String usuario) {
        Cliente cliente = clienteRepository.findByUsuario(usuario);
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario");
        }
        return cliente.getLibrosDeseados().stream().toList();
    }

    public Libro getLibroEnListaDeseados(Integer id, String usuario) {
        Cliente cliente = clienteRepository.findByUsuario(usuario);
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario");
        }
        return cliente.getLibrosDeseados().stream().filter(libro -> libro.getId().equals(id)).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Libro no está en lista de deseados"));
    }

    public List<Libro> getAllLibroPorBusqueda(String texto) {
        return libroRepository.findAllPorBusqueda(texto);
    }

    public List<Libro> getAllLibroPorBusquedaUser(String texto) {
        return libroRepository.findAllPorBusquedaUser(texto);
    }

    public List<Libro> getAllLibroPorBusquedaTipo(String tipoLibro, String texto) {
        return libroRepository.findAllPorBusquedaTipo(tipoLibro, texto);
    }

    public List<Libro> getAllLibroPorBusquedaTipoGenero(String tipoLibro, String genero, String texto) {
        return libroRepository.findAllPorBusquedaTipoGenero(tipoLibro, genero, texto);
    }

    public List<Libro> getAllPorNombreDeAutorBusqueda(String autor, String texto) {
        return libroRepository.findAllPorAutorBusqueda(autor, texto);
    }

    public List<Libro> getAllPorNombreDeEditorialBusqueda(String editorial, String texto) {
        return libroRepository.findAllPorEditorialBusqueda(editorial, texto);
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

    public void addLibroListaDeseados(Long id, Cliente clienteSoloUsuario) {
        Libro libro = libroRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el libro"));
        Cliente cliente = clienteRepository.findByUsuario(clienteSoloUsuario.getUsuario());
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario");
        }
        cliente.getLibrosDeseados().add(libro);
        clienteRepository.save(cliente);
    }

    public void deleteLibroListaDeseados(Long id, String usuario) {
        Libro libro = libroRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el libro"));
        Cliente cliente = clienteRepository.findByUsuario(usuario);
        if (cliente == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado el usuario");
        }
        if (cliente.getLibrosDeseados().contains(libro)) {
            cliente.getLibrosDeseados().remove(libro); 
            clienteRepository.save(cliente);
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El libro no estaba en la lista de deseados");
        }
    }

    public Libro actualizarStock(Long id, Integer addedStock) {
        Libro libroActualizar = getLibroPorId(id);
        Integer stockFinal = (libroActualizar.getStock()) + (addedStock); 
        libroActualizar.setStock(stockFinal);
        return libroRepository.save(libroActualizar);
    }
}

