package com.gert.tfgdam.api

import com.gert.tfgdam.model.TipoLibro
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TipoLibroApi {
    @GET("api/tipo-libro/get")
    suspend fun getAll(): Response<List<TipoLibro>>

    @GET("api/tipo-libro/get/{id}")
    suspend fun getPorId(@Path("id") id: Long): Response<TipoLibro>

    @POST("api/tipo-libro/new")
    suspend fun create(@Body tipoLibro: TipoLibro): Response<TipoLibro>

    @PUT("api/tipo-libro/update/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body tipoLibro: TipoLibro
    ): Response<TipoLibro>

    @DELETE("api/tipo-libro/delete/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Unit>
}