package com.example.reto1.ui.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.reto1.data.CommonFavouriteRepository
import com.example.reto1.data.Song
import com.example.reto1.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteViewModel(
private val favouritesRepository: CommonFavouriteRepository
) : ViewModel() {

    // TODO arreglar
    private val idUser = 1;

    private val _items = MutableLiveData<Resource<List<Song>>>()

    val items : LiveData<Resource<List<Song>>> get() = _items

    private val _deleted = MutableLiveData<Resource<Song>>()
    val deleted : LiveData<Resource<Song>> get() = _deleted
    // val delete: Int = _deleted.value!!.data.toString().toInt()
    init {
        val id =1
        updateFavouriteList(id)
    }

    fun updateFavouriteList(id: Int){
        viewModelScope.launch {
            val repoResponse = getFavouritesFromRepository();
            _items.value = repoResponse
        }
    }

    fun onFavoriteViewHolderClick(song: Song) {
        // cuando se hace click
    }
    fun onFavoriteDeleteClick(song: Song) {
        viewModelScope.launch {
            val response = deleteDepartment(song.id, idUser)
            when (response.status){
                Resource.Status.SUCCESS -> {
                    _deleted.value = Resource.success(song)
                }
                Resource.Status.ERROR -> {
                    _deleted.value = response.message?.let { Resource.error(it, song) }
                }
                Resource.Status.LOADING -> {

                }
            }
            updateFavouriteList(idUser);
        }
    }


    suspend fun deleteDepartment(id_song : Int, id_user : Int):Resource<Integer>{
        return withContext(Dispatchers.IO) {
            favouritesRepository.deleteFavorites(id_song, id_user)
        }
    }

    suspend fun getFavouritesFromRepository() : Resource<List<Song>> {
        return withContext(Dispatchers.IO){
            favouritesRepository.getFavorites()
        }
    }


}
class FavouritesViewModelFactory(
    private val favouriteRepository: CommonFavouriteRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return FavouriteViewModel(favouriteRepository) as T
    }
}