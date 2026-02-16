package com.gert.tfgdam.model

data class Libro(
    var id: Long? = null,
    var isbn: String? = null,
    var portada: String? = null,
    var titulo: String? = null,
    var editorial: Editorial? = null,
    var autor: Autor? = null,
    var tipoLibro: TipoLibro? = null,
    var fechaSalida: String? = null,
    var descripcion: String? = null,
    var precio: Double? = null,
    var stock: Integer? = null,
    var generos: List<Genero> = emptyList(),
)
