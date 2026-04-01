package com.gert.tfgdam.feature.admin.cliente.model

import com.gert.tfgdam.feature.admin.direccion.model.Direccion
import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.admin.cliente.model.Rol

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