package com.gert.tfgdam.feature.admin.genero.model

import com.gert.tfgdam.feature.admin.libro.model.Libro

data class Genero(
    var id: Long? = null,
    var nombre: String? = null,
    var libros: List<Libro> = emptyList(),
)