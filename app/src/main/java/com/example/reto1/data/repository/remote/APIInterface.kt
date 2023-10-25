package com.example.reto1.data.repository.remote

import com.example.reto1.data.Song
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIInterface {

    @GET("songs")
    suspend fun getSongs(): Response<List<Song>>

    @POST("songs")
    suspend fun createSong(@Body song: Song): Response<Integer>

    @GET("Songs/{id}")
    suspend fun getSong(@Path("id") id: Int): Response<Song>

}