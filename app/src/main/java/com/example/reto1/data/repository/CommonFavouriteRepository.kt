package com.example.reto1.data

import com.example.reto1.utils.Resource

interface CommonFavouriteRepository {

    suspend fun getFavorites() : Resource<List<Song>>

    suspend fun deleteFavorites(id_song: Int, id_user: Int) : Resource<Integer>

}