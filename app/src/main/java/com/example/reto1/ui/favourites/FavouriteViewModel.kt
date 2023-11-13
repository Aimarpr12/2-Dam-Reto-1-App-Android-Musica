package com.example.reto1.ui.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.reto1.MyApp
import com.example.reto1.data.CommonFavouriteRepository
import com.example.reto1.data.Song
import com.example.reto1.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteViewModel(
private val favouritesRepository: CommonFavouriteRepository
) : ViewModel() {


    private val _items = MutableLiveData<Resource<List<Song>>>()

    private lateinit var listaOriginal: List<Song>
    val items : LiveData<Resource<List<Song>>> get() = _items

    private val _deleted = MutableLiveData<Resource<Song>>()
    val deleted : LiveData<Resource<Song>> get() = _deleted

    private val _created = MutableLiveData<Resource<Song>>()
    val created : LiveData<Resource<Song>> get() = _created

    val userId : Int = MyApp.userPreferences.fetchUserId()!!

    init {
        updateFavouriteList()
    }

    fun updateFavouriteList(){
        viewModelScope.launch {
            _items.value = getFavouritesFromRepository()

            when (_items.value!!.status){
                Resource.Status.SUCCESS -> {
                    listaOriginal = ArrayList()
                    val lista = _items.value!!.data!!
                    if (lista != null) {
                        for (song in lista) {
                            (listaOriginal as ArrayList<Song>).add(song)
                        }
                    }
                }
                Resource.Status.ERROR -> {

                }
                Resource.Status.LOADING -> {

                }
            }
        }
    }

    fun filtrodeFavouriteList(autor: String?, cancion: String?){
        val listFavFiltradas = mutableListOf<Song>()
        listaOriginal.forEach { song ->
            if((song.author.lowercase().indexOf(autor!!.lowercase(), 0)) != -1
                && (song.title.lowercase().indexOf(cancion!!.lowercase(), 0)) != -1) {
                listFavFiltradas.add(song)
            }
        }
        if (listFavFiltradas.size == 0) {
            for (song in listaOriginal) {
                listFavFiltradas.add(song)
            }
        }
        val resource = Resource.success(listFavFiltradas)
        _items.value = resource
    }

    fun onFavoriteViewHolderClick(song: Song) {
        // cuando se hace click
    }

    fun onFavoriteDeleteClick(song: Song) {
        viewModelScope.launch {
            val response = deleteFavourite(song.id)
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
            updateFavouriteList()
        }
    }

    fun onFavoriteAddClick(song: Song) {
        viewModelScope.launch {
            val response = addFavourite(song.id)
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
            updateFavouriteList()
        }
    }

    suspend fun deleteFavourite(id_song : Int):Resource<Integer>{
        return withContext(Dispatchers.IO) {
            favouritesRepository.deleteFavorites(id_song)
        }
    }

    suspend fun addFavourite(id_song : Int):Resource<Integer>{
        return withContext(Dispatchers.IO) {
            favouritesRepository.createFavourite(id_song)
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