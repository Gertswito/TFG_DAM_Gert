package com.gert.tfgdam.api

import com.gert.tfgdam.model.Libro
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LibroApi {
    @GET("api/libro/get")
    suspend fun getAll(): Response<List<Libro>>

    @GET("api/libro/get/{id}")
    suspend fun getPorId(@Path("id") id: Long): Response<Libro>

    @POST("api/libro/new")
    suspend fun create(@Body libro: Libro): Response<Libro>

    @PUT("api/libro/update/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body libro: Libro
    ): Response<Libro>

    @DELETE("api/libro/delete/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Unit>
}