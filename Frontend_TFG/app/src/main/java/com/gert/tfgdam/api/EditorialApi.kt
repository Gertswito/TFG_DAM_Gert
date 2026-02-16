package com.gert.tfgdam.api

import com.gert.tfgdam.model.Editorial
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface EditorialApi {
    @GET("api/editorial/get")
    suspend fun getAll(): Response<List<Editorial>>

    @GET("api/editorial/get/{id}")
    suspend fun getPorId(@Path("id") id: Long): Response<Editorial>

    @POST("api/editorial/new")
    suspend fun create(@Body editorial: Editorial): Response<Editorial>

    @PUT("api/editorial/update/{id}")
    suspend fun update(
        @Path("id") id: Long,
        @Body editorial: Editorial
    ): Response<Editorial>

    @DELETE("api/editorial/delete/{id}")
    suspend fun delete(@Path("id") id: Long): Response<Unit>
}