package com.gert.tfgdam.repository

import com.gert.tfgdam.api.EditorialApi
import com.gert.tfgdam.model.Editorial
import com.gert.tfgdam.network.RetrofitClient
import retrofit2.Response

class EditorialRepository {

    private val api: EditorialApi =
        RetrofitClient.instance.create(EditorialApi::class.java)

    suspend fun getAll(): Response<List<Editorial>> {
        return api.getAll()
    }

    suspend fun getPorId(id: Long): Response<Editorial> {
        return api.getPorId(id)
    }

    suspend fun create(editorial: Editorial): Response<Editorial> {
        return api.create(editorial)
    }

    suspend fun update(id: Long, editorial: Editorial): Response<Editorial> {
        return api.update(id, editorial)
    }

    suspend fun delete(id: Long): Response<Unit> {
        return api.delete(id)
    }
}