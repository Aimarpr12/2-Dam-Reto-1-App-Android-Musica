package com.example.reto1.ui.songs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.reto1.data.Song
import com.example.reto1.data.repository.CommonSongRepository
import com.example.reto1.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SongViewModelById(
    private val songRepository: CommonSongRepository
) : ViewModel(){

    private val _items = MutableLiveData<Resource<List<Song>>>()
    val items: LiveData<Resource<List<Song>>> get() = _items

    private val _created = MutableLiveData<Resource<Integer>>();
    val created: LiveData<Resource<Integer>> get() = _created;

  /*  private suspend fun getSongByIdFromRepository(id : Int): Resource<Song>{
        return withContext(Dispatchers.IO){
            songRepository.getSongById(id)
        }

    }*/
}