package com.gert.tfgdam.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Cliente;
import com.gert.tfgdam.entity.Rol;
import com.gert.tfgdam.repository.ClienteRepository;
import com.gert.tfgdam.security.JwtTokenUtil;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;

    public ClienteService(ClienteRepository clienteRepository, BCryptPasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.clienteRepository = clienteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public List<Cliente> getAllCliente() {
        return clienteRepository.findAll();
    }

    public Cliente getClientePorId(Long id) {
        return clienteRepository.findWithDireccionesById(id.intValue()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado al usuario"));    
    }

    public Cliente getClientePorUsuario(String usuario) { 
        return clienteRepository.findWithDireccionesByUsuario(usuario).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado al usuario"));    
    }

    public String login(Cliente cliente) {
        if (!clienteRepository.existsByUsuario(cliente.getUsuario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha encontrado al usuario");
        }
        Cliente usuario = clienteRepository.findByUsuario(cliente.getUsuario());

        if (!passwordEncoder.matches(cliente.getContrasenha(), usuario.getContrasenha())) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La contraseña es incorrecta");
        }

        return jwtTokenUtil.generateToken(usuario.getUsuario(), usuario.getRol());
    }

    public Cliente cambiarContrasenha(Long id, String contrasenha) {
        Cliente clienteExistente = clienteRepository.findById(id).orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado al usuario"));

        BCryptPasswordEncoder newPasswordEncoder = new BCryptPasswordEncoder();
        clienteExistente.setContrasenha(newPasswordEncoder.encode(contrasenha));

        return clienteRepository.save(clienteExistente);
    }

    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado al usuario");
        }
        try {
            clienteRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este usuario no puede ser eliminado", e);
        }
    }

    public Cliente save(Cliente cliente) {
        if (clienteRepository.existsByUsuario(cliente.getUsuario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario ya está en uso");
        }
        if (cliente.getEmail() != null && clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico ya está en uso");
        }

        if (cliente.getRol() == null) {
            cliente.setRol(Rol.USER);
        }

        BCryptPasswordEncoder newPasswordEncoder = new BCryptPasswordEncoder();
        cliente.setContrasenha(newPasswordEncoder.encode(cliente.getContrasenha()));

        return clienteRepository.save(cliente);
    }

    public Cliente update(Cliente cliente) {
        Cliente clienteExistente = clienteRepository.findByUsuario(cliente.getUsuario());
        if (clienteExistente != null && !clienteExistente.getId().equals(cliente.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de usuario ya está en uso");
        }

        Cliente clienteSinActualizar = clienteRepository.findById(cliente.getId().longValue()).orElseThrow(() ->new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado al usuario"));
        clienteSinActualizar.setEmail(cliente.getEmail());
        clienteSinActualizar.setNombre(cliente.getNombre());
        clienteSinActualizar.setApellidos(cliente.getApellidos());
        
        if (cliente.getContrasenha() != null && !cliente.getContrasenha().isEmpty()) {
            BCryptPasswordEncoder newPasswordEncoder = new BCryptPasswordEncoder();
            clienteSinActualizar.setContrasenha(newPasswordEncoder.encode(cliente.getContrasenha()));
        }

        if (cliente.getDirecciones() != null && !cliente.getDirecciones().isEmpty()) {
            clienteSinActualizar.setDirecciones(cliente.getDirecciones());
        }

        if (cliente.getLibrosDeseados() != null && !cliente.getLibrosDeseados().isEmpty()) {
            clienteSinActualizar.setLibrosDeseados(cliente.getLibrosDeseados());
        }

        if (cliente.getRol() != null) {
            clienteSinActualizar.setRol(cliente.getRol());
        }

        return clienteRepository.save(clienteSinActualizar);
    }
}
