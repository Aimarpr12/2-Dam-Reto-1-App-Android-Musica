package com.example.reto1.ui.songs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.reto1.MyApp
import com.example.reto1.data.Song
import com.example.reto1.data.repository.CommonSongRepository
import com.example.reto1.data.repository.remote.RemoteSongsDataSource
import com.example.reto1.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongsViewModelFactory(
    private val songRepository: RemoteSongsDataSource
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return SongsViewModel(songRepository) as T
    }

}
class SongsViewModel (
    private val songRepository: CommonSongRepository
) : ViewModel(){

    private val _items = MutableLiveData<Resource<List<Song>>>()
    private lateinit var listaOriginal: List<Song>
    val items: LiveData<Resource<List<Song>>> get() = _items

    private val _created = MutableLiveData<Resource<Integer>>()
    val created: LiveData<Resource<Integer>> get() = _created

    private val _delete = MutableLiveData<Resource<Integer>>()
    val deleted: LiveData<Resource<Integer>> get() = _delete

    var deletedName: String = "";

    val userID = MyApp.userPreferences.fetchUserId()!!
    init {
        updateSongList(userID)
    }




    fun updateSongList(id_user: Int) {
        viewModelScope.launch {
            val repoResponse = getSongsFromRepository(id_user);
            _items.value = repoResponse
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
    fun filtrodeSongList(autor: String?, cancion: String?){
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

    suspend fun getSongsFromRepository(id_user: Int): Resource<List<Song>> {
        return withContext(Dispatchers.IO) {
            songRepository.getSongs(id_user)
        }
    }
    fun onAddSong( url: String, title: String, author: String){
        val newSong = Song( url, title, author, false)
        viewModelScope.launch {
            _created.value = createNewSong(newSong)
        }
    }

    fun onDeleteSong(song: Song){
        viewModelScope.launch {
            _delete.value = deleteSong(song.id)
            deletedName = song.title
        }
    }

    suspend fun createNewSong(song: Song): Resource<Integer> {
        return withContext(Dispatchers.IO) {
            songRepository.createSong(song)
        }

    }
    suspend fun deleteSong(id: Int): Resource<Integer> {
        return withContext(Dispatchers.IO) {
            songRepository.deleteSong(id)
        }

    }
}