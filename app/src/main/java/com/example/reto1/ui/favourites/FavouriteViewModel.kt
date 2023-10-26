package com.example.reto1.ui.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.reto1.data.CommonFavouriteRepository
import com.example.reto1.data.Favourite
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

    private val _created = MutableLiveData<Resource<Song>>()
    val created : LiveData<Resource<Song>> get() = _created


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
            val response = deleteFavourite(song.id, 1)
            when (response.status){
                Resource.Status.SUCCESS -> {
                    //_delete.value = Resource.success(response.data.toString().toInt())
                    _deleted.value = Resource.success(song)
                }
                Resource.Status.ERROR -> {
                    _deleted.value = response.message?.let { Resource.error(it, song) }
                }
                Resource.Status.LOADING -> {

                }
            }
            updateFavouriteList(idUser)
        }
    }

    fun onFavoriteAddClick(song: Song) {
        viewModelScope.launch {
            val response = addFavourite(song.id, idUser)
            when (response.status){
                Resource.Status.SUCCESS -> {
                    _created.value = Resource.success(song)
                }
                Resource.Status.ERROR -> {
                    _created.value = response.message?.let { Resource.error(it, song) }
                }
                Resource.Status.LOADING -> {

                }
            }
            updateFavouriteList(idUser)
        }
    }


    suspend fun deleteFavourite(id_song : Int, id_user : Int):Resource<Integer>{
        return withContext(Dispatchers.IO) {
            favouritesRepository.deleteFavorites(id_song, id_user)
        }
    }

    suspend fun addFavourite(id_song : Int, id_user : Int):Resource<Integer>{
        val fav = Favourite(0, id_user, id_song)
        return withContext(Dispatchers.IO) {
            favouritesRepository.createFavourite(fav)
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