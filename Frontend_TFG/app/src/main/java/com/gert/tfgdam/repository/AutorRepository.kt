package com.gert.tfgdam.repository

import com.gert.tfgdam.model.Autor
import com.gert.tfgdam.network.RetrofitClient
import com.gert.tfgdam.api.AutorApi
import retrofit2.Response

class AutorRepository {

    private val api: AutorApi =
        RetrofitClient.instance.create(AutorApi::class.java)

    suspend fun getAll(): Response<List<Autor>> {
        return api.getAll()
    }

    suspend fun getPorId(id: Long): Response<Autor> {
        return api.getPorId(id)
    }

    suspend fun create(autor: Autor): Response<Autor> {
        return api.create(autor)
    }

    suspend fun update(id: Long, autor: Autor): Response<Autor> {
        return api.update(id, autor)
    }

    suspend fun delete(id: Long): Response<Unit> {
        return api.delete(id)
    }
}