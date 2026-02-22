package com.gert.tfgdam.model

data class Cliente(
    var id: Long? = null,
    var rol: Rol? = null,
    var nombre: String? = null,
    var apellidos: String? = null,
    var usuario: String? = null,
    var email: String? = null,
    var direcciones: List<Direccion> = emptyList(),
    var librosDeseados: Set<Libro> = emptySet(),
    var contrasenha: String? = null
)
