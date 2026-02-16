package com.gert.tfgdam.repository

import com.gert.tfgdam.api.GeneroApi
import com.gert.tfgdam.model.Genero
import com.gert.tfgdam.network.RetrofitClient
import retrofit2.Response

class GeneroRepository {

    private val api: GeneroApi =
        RetrofitClient.instance.create(GeneroApi::class.java)

    suspend fun getAll(): Response<List<Genero>> {
        return api.getAll()
    }

    suspend fun getPorId(id: Long): Response<Genero> {
        return api.getPorId(id)
    }

    suspend fun create(genero: Genero): Response<Genero> {
        return api.create(genero)
    }

    suspend fun update(id: Long, genero: Genero): Response<Genero> {
        return api.update(id, genero)
    }

    suspend fun delete(id: Long): Response<Unit> {
        return api.delete(id)
    }
}