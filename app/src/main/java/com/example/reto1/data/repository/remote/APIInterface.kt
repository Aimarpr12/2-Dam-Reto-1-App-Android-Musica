package com.example.reto1.data.repository.remote

import com.example.reto1.data.Song
import retrofit2.Response
import retrofit2.http.GET

interface APIInterface {

    @GET("songs")
    suspend fun getSongs(): Response<List<Song>>
}