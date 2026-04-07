package com.gert.tfgdam.service;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import com.gert.tfgdam.entity.Cliente;
import com.gert.tfgdam.entity.Rol;
import com.gert.tfgdam.repository.ClienteRepository;
import com.gert.tfgdam.security.JwtTokenUtil;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void inicializarCliente() {
        cliente = new Cliente();
        cliente.setId(1);
        cliente.setNombre("Nombre Test");
        cliente.setApellidos("Apellidos Test");
        cliente.setUsuario("usuarioTest");
        cliente.setEmail("test@email.com");
        cliente.setContrasenha("12345");
        cliente.setRol(Rol.USER);
    }

    @Test
    void getAllCliente_ok() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));
        List<Cliente> result = clienteService.getAllCliente();
        assertEquals(1, result.size());
        verify(clienteRepository).findAll();
    }

    @Test
    void getClientePorId_ok() {
        when(clienteRepository.findWithDireccionesById(1)).thenReturn(Optional.of(cliente));
        Cliente result = clienteService.getClientePorId(1L);
        assertEquals("Nombre Test", result.getNombre());
    }

    @Test
    void getClientePorId_notFound() {
        when(clienteRepository.findWithDireccionesById(1)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> clienteService.getClientePorId(1L));
    }

    @Test
    void getClientePorUsuario_ok() {
        when(clienteRepository.findWithDireccionesByUsuario("usuarioTest")).thenReturn(Optional.of(cliente));
        Cliente result = clienteService.getClientePorUsuario("usuarioTest");
        assertEquals("Nombre Test", result.getNombre());
    }

    @Test
    void getClientePorUsuario_notFound() {
        when(clienteRepository.findWithDireccionesByUsuario("usuarioTest")).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> clienteService.getClientePorUsuario("usuarioTest"));
    }

    @Test
    void login_ok() {
        when(clienteRepository.existsByUsuario("usuarioTest")).thenReturn(true);
        when(clienteRepository.findByUsuario("usuarioTest")).thenReturn(cliente);
        when(passwordEncoder.matches("12345", "12345")).thenReturn(true);
        when(jwtTokenUtil.generateToken(cliente.getUsuario(), cliente.getRol())).thenReturn("token123");

        String token = clienteService.login(cliente);
        assertEquals("token123", token);
    }

    @Test
    void login_usuarioNoEncontrado() {
        when(clienteRepository.existsByUsuario("usuarioTest")).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> clienteService.login(cliente));
    }

    @Test
    void login_contrasenhaIncorrecta() {
        when(clienteRepository.existsByUsuario("usuarioTest")).thenReturn(true);
        // Cliente real tiene contraseña "12345", pasamos otra distinta para que falle
        cliente.setContrasenha("wrongPass");

        when(clienteRepository.findByUsuario("usuarioTest")).thenReturn(new Cliente() {{
            setContrasenha("12345");
        }});

        assertThrows(ResponseStatusException.class, () -> clienteService.login(cliente));
    }

    @Test
    void cambiarContrasenha_ok() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any())).thenReturn(cliente);

        Cliente result = clienteService.cambiarContrasenha(1L, "nuevaPass");
        assertNotNull(result);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void cambiarContrasenha_notFound() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> clienteService.cambiarContrasenha(1L, "nuevaPass"));
    }

    @Test
    void delete_ok() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1L);

        clienteService.delete(1L);
        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void delete_notFound() {
        when(clienteRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResponseStatusException.class, () -> clienteService.delete(1L));
    }

    @Test
    void delete_conflict() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doThrow(DataIntegrityViolationException.class).when(clienteRepository).deleteById(1L);

        assertThrows(ResponseStatusException.class, () -> clienteService.delete(1L));
    }

    @Test
    void save_ok() {
        when(clienteRepository.existsByUsuario("usuarioTest")).thenReturn(false);
        when(clienteRepository.existsByEmail("test@email.com")).thenReturn(false);
        when(clienteRepository.save(any())).thenReturn(cliente);

        Cliente result = clienteService.save(cliente);
        assertNotNull(result);
        verify(clienteRepository).save(any());
    }

    @Test
    void save_usuarioDuplicado() {
        when(clienteRepository.existsByUsuario("usuarioTest")).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> clienteService.save(cliente));
    }

    @Test
    void save_emailDuplicado() {
        when(clienteRepository.existsByUsuario("usuarioTest")).thenReturn(false);
        when(clienteRepository.existsByEmail("test@email.com")).thenReturn(true);
        assertThrows(ResponseStatusException.class, () -> clienteService.save(cliente));
    }
}