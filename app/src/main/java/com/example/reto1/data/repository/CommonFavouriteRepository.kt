package com.example.reto1.data

import com.example.reto1.utils.Resource

interface CommonFavouriteRepository {

    suspend fun getFavorites() : Resource<List<Song>>

    suspend fun deleteFavorites(id_song: Int) : Resource<Integer>

    suspend fun  createFavourite(id_song: Int) : Resource<Integer>

}