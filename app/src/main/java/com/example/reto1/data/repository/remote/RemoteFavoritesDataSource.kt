package com.example.reto1.data.repository.remote

import com.example.reto1.data.CommonFavouriteRepository

class RemoteFavoritesDataSource: BaseDataSource(), CommonFavouriteRepository {

    override suspend fun getFavorites()= getResult{
        RetrofitClient.apiInterface.getFavorites()
    }

    override suspend fun deleteFavorites(id_song: Int)= getResult{
        RetrofitClient.apiInterface.deleteFavorites(id_song)
    }

    override suspend fun createFavourite(id_song: Int)= getResult{
        RetrofitClient.apiInterface.createFavourite(id_song)
    }

}