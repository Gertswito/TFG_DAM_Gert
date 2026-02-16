package com.gert.tfgdam.api

import com.gert.tfgdam.model.Autor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AutorApi {

    @GET("api/autor/get")
    suspend fun getAll(): Response<List<Autor>>

    @GET("api/autor/get/{id}")
    suspend fun getPorId(@Path("id") id: Long): Response<Autor>

    @POST("api/autor/new")
    suspend fun create(@Body autor: Autor): Response<Autor>

    @PUT("api/autor/update/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body autor: Autor
    ): Response<Autor>

    @DELETE("api/autor/delete/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Unit>
}