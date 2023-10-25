package com.example.reto1.ui.songs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.reto1.data.repository.CommonSongRepository
import com.example.reto1.data.repository.remote.RemoteSongsDataSource
import com.example.reto1.data.Song
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
    val items: LiveData<Resource<List<Song>>> get() = _items

    private val _created = MutableLiveData<Resource<Integer>>();
    val created: LiveData<Resource<Integer>> get() = _created;

    init {
        updateSongList()
    }

    fun updateSongList() {
        viewModelScope.launch {
            // lista a mano
            // voy a llamar a la funcion
            // que va a solicitar los empleados del repositorio
            val repoResponse = getSongsFromRepository();
            // cambiamos el valor de mutableLiveData
            _items.value = repoResponse
        }
    }

    suspend fun getSongsFromRepository(): Resource<List<Song>> {
        return withContext(Dispatchers.IO) {
            // aqui unicamente llamamos a la funcion del repositorio
            songRepository.getSongs()
        }
    }
    fun onAddEmployee( url: String, title: String, author: String){
        val newSong = Song( url, title, author)
        viewModelScope.launch {
            _created.value = createNewSong(newSong)
        }
    }
    suspend fun createNewSong(song: Song): Resource<Integer> {
        return withContext(Dispatchers.IO) {
            songRepository.createSong(song)
        }

    }


    }