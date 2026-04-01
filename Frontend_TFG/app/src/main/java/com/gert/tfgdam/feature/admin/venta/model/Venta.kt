package com.gert.tfgdam.feature.admin.venta.model

import com.gert.tfgdam.feature.admin.cliente.model.Cliente
import com.gert.tfgdam.feature.admin.direccion.model.Direccion

data class Venta(
    var id: Long? = null,
    var cliente: Cliente? = null,
    var direccion: Direccion? = null,
    var fecha: String? = null,
    var hora: String? = null,
    var precioFinal: Double? = null
)