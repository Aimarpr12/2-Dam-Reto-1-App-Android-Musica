package com.example.reto1.data.repository.remote

import com.example.reto1.data.Song
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST

interface APIInterface {

    @GET("songs")
    suspend fun getSongs(): Response<List<Song>>

    @POST("songs")
    suspend fun createSong(@Body song: Song): Response<Integer>

    @GET("Songs/{id}")
    suspend fun getSong(@Path("id") id: Int): Response<Song>


    @GET("fav/{id}")
    suspend fun getFavorites(@Path("id") id: Int): Response<List<Song>>

    @DELETE("fav/{id_song}/{id_user}")
    suspend fun deleteFavorites(@Path("id_song") id_song: Int, @Path("id_user") id_user: Int): Response<Integer>


}