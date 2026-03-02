package com.gert.tfgdam.repository

import com.gert.tfgdam.api.LibroApi
import com.gert.tfgdam.model.Cliente
import com.gert.tfgdam.model.Libro
import com.gert.tfgdam.network.RetrofitClient
import retrofit2.Response

class LibroRepository {

    private val api: LibroApi =
        RetrofitClient.instance.create(LibroApi::class.java)

    suspend fun getAll(): Response<List<Libro>> {
        return api.getAll()
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

    suspend fun getPorId(id: Long): Response<Libro> {
        return api.getPorId(id)
    }

    suspend fun getAllListaDeseados(usuario: String): Response<List<Libro>> {
        return api.getAllListaDeseados(usuario)
    }

    suspend fun getLibroEnListaDeseados(id: Long, usuario: String): Response<Libro> {
        return api.getLibroEnListaDeseados(id, usuario)
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
}