package com.example.reto1.data

import com.example.reto1.utils.Resource

interface CommonFavouriteRepository {

    suspend fun getFavorites(id_user: Int) : Resource<List<Song>>

    suspend fun deleteFavorites(id_song: Int, id_user: Int) : Resource<Integer>

    suspend fun  createFavourite(favourite: Favourite) : Resource<Integer>

}