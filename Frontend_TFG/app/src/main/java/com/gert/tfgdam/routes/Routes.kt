package com.gert.tfgdam.routes

object Routes {
    const val HOME = "home"
    const val HOME_ADMIN = "home_admin"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val USER_SETTINGS = "user_settings"
    const val TIPO_LIBRO_GENEROS = "tipo_libro_generos/{tipoLibro}"
    const val TIPO_LIBRO_GENERO_SELECTED = "tipo_libro_genero_selected/{tipoLibro}/{genero}"
    const val LIBRO_DETAILS = "libro_details/{libroId}"
    const val LIBROS_POR_AUTOR = "libros_por_autor/{autor}"
    const val LIBROS_POR_EDITORIAL = "libros_por_editorial/{editorial}"
    const val CARRITO = "carrito"
    const val HISTORIAL_COMPRA = "historial_compra"
    const val LISTA_DESEADOS = "lista_deseados"
    const val PAGO = "pago"
    const val COMPRA_FINALIZADA = "compra_finalizada/{orderId}"
}