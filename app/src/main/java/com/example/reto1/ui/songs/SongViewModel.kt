package com.example.reto1.ui.songs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
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
    val items: LiveData<Resource<List<Song>>> get() = _items

    private val _created = MutableLiveData<Resource<Integer>>()
    val created: LiveData<Resource<Integer>> get() = _created

    private val _delete = MutableLiveData<Resource<Integer>>()
    val deleted: LiveData<Resource<Integer>> get() = _delete

    var deletedName: String = "";
    init {
        updateSongList(1)
    }




    fun updateSongList(id_user: Int) {
        viewModelScope.launch {
            val repoResponse = getSongsFromRepository(id_user);
            _items.value = repoResponse
        }
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