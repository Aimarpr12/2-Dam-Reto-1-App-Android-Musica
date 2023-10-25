package com.example.reto1.data.repository

import com.example.reto1.data.CommonFavouriteRepository

class FavouriteRepository(private val repository: CommonFavouriteRepository) {

    suspend fun getFavorites() = repository.getFavorites()

}