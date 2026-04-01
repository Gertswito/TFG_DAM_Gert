package com.gert.tfgdam.feature.admin.libro.model

import com.gert.tfgdam.feature.admin.autor.model.Autor
import com.gert.tfgdam.feature.admin.editorial.model.Editorial
import com.gert.tfgdam.feature.admin.genero.model.Genero
import com.gert.tfgdam.feature.admin.tipolibro.model.TipoLibro

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
    var stock: Int? = null,
    var generos: List<Genero> = emptyList(),
)