package com.gert.tfgdam.model

data class Genero(
    var id: Long? = null,
    var nombre: String? = null,
    var libros: List<Libro> = emptyList(),
)
