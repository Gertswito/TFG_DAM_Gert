package com.gert.tfgdam.feature.admin.libro.repository

import com.gert.tfgdam.feature.admin.libro.api.LibroApi
import com.gert.tfgdam.core.network.RetrofitClient
import com.gert.tfgdam.feature.admin.cliente.model.Cliente
import com.gert.tfgdam.feature.admin.libro.model.Libro
import retrofit2.Response

class LibroRepository {

    private val api: LibroApi =
        RetrofitClient.instance.create(LibroApi::class.java)

    suspend fun getAll(): Response<List<Libro>> {
        return api.getAll()
    }

    suspend fun getAllLimitadoPorStock(): Response<List<Libro>> {
        return api.getAllLimitadoPorStock()
    }

    suspend fun getAllLimitadoParaView(): Response<List<Libro>> {
        return api.getAllLimitadoParaView()
    }

    suspend fun getAllPorTipo(tipoLibro: String): Response<List<Libro>> {
        return api.getAllPorTipo(tipoLibro)
    }

    suspend fun getAllPorTipoGenero(tipoLibro: String, genero: String): Response<List<Libro>> {
        return api.getAllPorTipoGenero(tipoLibro, genero)
    }

    suspend fun getAllPorNombreDeAutor(autor: String): Response<List<Libro>> {
        return api.getAllPorNombreDeAutor(autor)
    }

    suspend fun getAllPorNombreDeEditorial(editorial: String): Response<List<Libro>> {
        return api.getAllPorNombreDeEditorial(editorial)
    }

    suspend fun getAllNovedadesPorMes(): Response<List<Libro>> {
        return api.getAllNovedadesPorMes()
    }

    suspend fun getAllNovedadesPorUltimaAdicion(): Response<List<Libro>> {
        return api.getAllNovedadesPorUltimaAdicion()
    }

    suspend fun getPorId(id: Long): Response<Libro> {
        return api.getPorId(id)
    }

    suspend fun getAllListaDeseados(usuario: String): Response<List<Libro>> {
        return api.getAllListaDeseados(usuario)
    }

    suspend fun getLibroEnListaDeseados(id: Long, usuario: String): Response<Libro> {
        return api.getLibroEnListaDeseados(id, usuario)
    }

    suspend fun getAllPorBusqueda(texto: String): Response<List<Libro>> {
        return api.getAllPorBusqueda(texto)
    }

    suspend fun getAllPorBusquedaUser(texto: String): Response<List<Libro>> {
        return api.getAllPorBusquedaUser(texto)
    }

    suspend fun getAllPorBusquedaTipo(texto: String, tipoLibro: String): Response<List<Libro>> {
        return api.getAllPorBusquedaTipo(tipoLibro, texto)
    }

    suspend fun getAllPorBusquedaTipoGenero(texto: String, tipoLibro: String, genero: String): Response<List<Libro>> {
        return api.getAllPorBusquedaTipoGenero(tipoLibro, genero, texto)
    }

    suspend fun getAllPorNombreDeAutorBusqueda(autor: String, texto: String): Response<List<Libro>> {
        return api.getAllPorNombreDeAutorBusqueda(autor, texto)
    }

    suspend fun getAllPorNombreDeEditorialBusqueda(editorial: String, texto: String): Response<List<Libro>> {
        return api.getAllPorNombreDeEditorialBusqueda(editorial, texto)
    }

    suspend fun create(libro: Libro): Response<Libro> {
        return api.create(libro)
    }

    suspend fun update(id: Long, libro: Libro): Response<Libro> {
        return api.update(id, libro)
    }

    suspend fun delete(id: Long): Response<Unit> {
        return api.delete(id)
    }

    suspend fun addLibroListaDeseados(id: Long, cliente: Cliente): Response<Unit> {
        return api.addLibroListaDeseados(id, cliente)
    }

    suspend fun deleteLibroListaDeseados(id: Long, usuario: String): Response<Unit> {
        return api.deleteLibroListaDeseados(id, usuario)
    }

    suspend fun actualizarStock(id: Long, stock: Int): Response<Libro> {
        return api.actualizarStock(id, stock)
    }
}