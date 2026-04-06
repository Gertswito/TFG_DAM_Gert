package com.gert.tfgdam.feature.admin.direccion.model

import com.gert.tfgdam.feature.admin.cliente.model.Cliente

data class Direccion(
    var id: Long? = null,
    var calle: String? = null,
    var numero: Int? = null,
    var piso: String? = null,
    var ciudad: String? = null,
    var provincia: String? = null,
    var codigoPostal: String? = null,
    var activo: Boolean? = null,
    var cliente: Cliente? = null
)