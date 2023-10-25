package com.example.reto1.data.repository.remote

import com.example.reto1.data.CommonFavouriteRepository

class RemoteFavoritesDataSource: BaseDataSource(), CommonFavouriteRepository {

    override suspend fun getFavorites()= getResult{
        RetrofitClient.apiInterface.getFavorites(1)
    }

    override suspend fun deleteFavorites(id_song: Int, id_user: Int)= getResult{
        RetrofitClient.apiInterface.deleteFavorites(id_song, id_user)
    }
}