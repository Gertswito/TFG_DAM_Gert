package com.gert.tfgdam.feature.user.carrito.model

import com.gert.tfgdam.feature.admin.libro.model.Libro

data class CarritoItem(
    val libro: Libro,
    val cantidad: Int
)