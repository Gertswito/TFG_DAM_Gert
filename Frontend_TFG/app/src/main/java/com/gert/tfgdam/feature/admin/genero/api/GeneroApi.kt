package com.gert.tfgdam.feature.admin.genero.api

import com.gert.tfgdam.feature.admin.genero.model.Genero
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GeneroApi {
    @GET("api/genero/get")
    suspend fun getAll(): Response<List<Genero>>

    @GET("api/genero/get/{id}")
    suspend fun getPorId(@Path("id") id: Long): Response<Genero>

    @POST("api/genero/new")
    suspend fun create(@Body genero: Genero): Response<Genero>

    @PUT("api/genero/update/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body genero: Genero
    ): Response<Genero>

    @DELETE("api/genero/delete/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Unit>
}