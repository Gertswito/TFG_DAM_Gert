package com.gert.tfgdam.repository

import com.gert.tfgdam.api.LibroApi
import com.gert.tfgdam.model.Libro
import com.gert.tfgdam.network.RetrofitClient
import retrofit2.Response

class LibroRepository {

    private val api: LibroApi =
        RetrofitClient.instance.create(LibroApi::class.java)

    suspend fun getAll(): Response<List<Libro>> {
        return api.getAll()
    }

    suspend fun getPorId(id: Long): Response<Libro> {
        return api.getPorId(id)
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
}