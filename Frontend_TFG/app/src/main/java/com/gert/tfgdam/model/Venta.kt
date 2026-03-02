package com.gert.tfgdam.model

data class Venta(
    var id: Long? = null,
    var cliente: Cliente? = null,
    var direccion: Direccion? = null,
    var fecha: String? = null,
    var hora: String? = null,
    var precioFinal: Double? = null
)

