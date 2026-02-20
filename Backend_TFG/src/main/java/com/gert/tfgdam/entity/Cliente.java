package com.gert.tfgdam.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

@Entity
@SuppressWarnings("common-java:DuplicatedBlocks")
@Table(name = "cliente")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol = Rol.USER;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "apellidos", nullable = false)
    private String apellidos;

    @Column(name = "usuario", nullable = false, unique = true)
    private String usuario;

    @Column(name = "email", unique = true)
    private String email;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Direccion> direcciones = new ArrayList<>();    

    @ManyToMany
    @JoinTable(
        name = "cliente_libro_favoritos",
        joinColumns = @JoinColumn(name = "cliente_id"),
        inverseJoinColumns = @JoinColumn(name = "libro_id")
    )
    private Set<Libro> librosFavoritos = new HashSet<>();

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "contrasenha")
    private String contrasenha;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Direccion> getDirecciones() {
        return direcciones;
    }

    public void setDirecciones (List<Direccion> direcciones) {
        this.direcciones = direcciones;
    }

    public Set<Libro> getLibrosFavoritos() {
        return librosFavoritos;
    }

    public void setLibrosFavoritos(Set<Libro> librosFavoritos) {
        this.librosFavoritos = librosFavoritos;
    }

    public String getContrasenha() {
        return contrasenha;
    }

    public void setContrasenha(String contrasenha) {
        this.contrasenha = contrasenha;
    }
}