package com.gert.tfgdam.model

data class Direccion(
    var id: Long? = null,
    var calle: String? = null,
    var numero: Int? = null,
    var piso: String? = null,
    var ciudad: String? = null,
    var provincia: String? = null,
    var codigoPostal: String? = null,
    var cliente: Cliente? = null
)
