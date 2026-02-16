package com.gert.tfgdam.repository

import com.gert.tfgdam.api.TipoLibroApi
import com.gert.tfgdam.model.TipoLibro
import com.gert.tfgdam.network.RetrofitClient
import retrofit2.Response

class TipoLibroRepository {

    private val api: TipoLibroApi =
        RetrofitClient.instance.create(TipoLibroApi::class.java)

    suspend fun getAll(): Response<List<TipoLibro>> {
        return api.getAll()
    }

    suspend fun getPorId(id: Long): Response<TipoLibro> {
        return api.getPorId(id)
    }

    suspend fun create(tipoLibro: TipoLibro): Response<TipoLibro> {
        return api.create(tipoLibro)
    }

    suspend fun update(id: Long, tipoLibro: TipoLibro): Response<TipoLibro> {
        return api.update(id, tipoLibro)
    }

    suspend fun delete(id: Long): Response<Unit> {
        return api.delete(id)
    }
}