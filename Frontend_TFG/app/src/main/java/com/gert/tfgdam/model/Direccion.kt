package com.gert.tfgdam.model

data class Direccion(
    var id: Long? = null,
    var calle: String? = null,
    var numero: Integer? = null,
    var piso: String? = null,
    var ciudad: String? = null,
    var provincia: String? = null,
    var codigoPostal: String? = null,
)
