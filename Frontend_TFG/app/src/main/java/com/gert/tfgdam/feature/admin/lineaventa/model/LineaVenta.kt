package com.gert.tfgdam.feature.admin.lineaventa.model

import com.gert.tfgdam.feature.admin.libro.model.Libro
import com.gert.tfgdam.feature.admin.venta.model.Venta

data class LineaVenta(
    var id: Long? = null,
    var venta: Venta? = null,
    var libro: Libro? = null,
    var cantidad: Int? = null,
    var precioParcial: Double? = null,
    var precioTotal: Double? = null,
)