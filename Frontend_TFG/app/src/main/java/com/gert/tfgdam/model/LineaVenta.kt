package com.gert.tfgdam.model

data class LineaVenta(
    var id: Long? = null,
    var venta: Venta? = null,
    var libro: Libro? = null,
    var cantidad: Integer? = null,
    var precioParcial: Double? = null,
    var precioTotal: Double? = null,
)

