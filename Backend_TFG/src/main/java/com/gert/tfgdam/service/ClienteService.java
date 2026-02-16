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
        return clienteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "clienteNoExiste"));
    }

    public String login(Cliente cliente) {
        if (!clienteRepository.existsByUsuario(cliente.getUsuario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "usuarioNoExiste");
        }
        Cliente usuario = clienteRepository.findByUsuario(cliente.getUsuario());

        if (!passwordEncoder.matches(cliente.getContrasenha(), usuario.getContrasenha())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "contrasenhaIncorrecta");
        }

        return jwtTokenUtil.generateToken(usuario.getUsuario(), usuario.getRol());
    }

    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "clienteNoExiste");
        }
        try {
            clienteRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "clienteNoSePuedeEliminar", e);
        }
    }

    public Cliente save(Cliente cliente) {
        if (clienteRepository.existsByUsuario(cliente.getUsuario())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombreUsuarioExiste");
        }
        if (cliente.getEmail() != null && clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "emailExiste");
        }
        if (cliente.getDni() != null && clienteRepository.existsByDni(cliente.getDni())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "dniExiste");
        }

        if (cliente.getRol() == null) {
            cliente.setRol(Rol.USER);
        }

        BCryptPasswordEncoder newPasswordEncoder = new BCryptPasswordEncoder();
        cliente.setContrasenha(newPasswordEncoder.encode(cliente.getContrasenha()));

        return clienteRepository.save(cliente);
    }

    public Cliente update(Cliente cliente) {
        Cliente existente = clienteRepository.findByUsuario(cliente.getUsuario());
        if (existente != null && !existente.getId().equals(cliente.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombreExiste");
        }
        return clienteRepository.save(cliente);
    }
}
