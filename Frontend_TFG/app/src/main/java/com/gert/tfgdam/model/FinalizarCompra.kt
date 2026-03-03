package com.gert.tfgdam.model

data class FinalizarCompra(
    val venta: Venta,
    val lineasVenta: List<LineaVenta>
)
