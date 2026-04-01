package com.gert.tfgdam.feature.user.pago.model

import com.gert.tfgdam.feature.admin.lineaventa.model.LineaVenta
import com.gert.tfgdam.feature.admin.venta.model.Venta

data class FinalizarCompra(
    val venta: Venta,
    val lineasVenta: List<LineaVenta>
)