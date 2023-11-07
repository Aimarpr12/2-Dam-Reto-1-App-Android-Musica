package com.example.reto1.data.repository.remote
import com.example.reto1.data.Song
import com.example.reto1.data.repository.CommonSongRepository

class RemoteSongsDataSource: BaseDataSource(), CommonSongRepository {

    override suspend fun getSongs(id_user: Int) = getResult {
        RetrofitClient.apiInterface.getSongs(id_user)
    }
    override suspend fun createSong(song: Song) = getResult {
        RetrofitClient.apiInterface.createSong(song)
    }

    override suspend fun deleteSong(id: Int)= getResult {
        RetrofitClient.apiInterface.deleteSong(id)
    }
    /*override suspend fun getSongById(id: Int) = getResult {
         RetrofitClient.apiInterface.getSong(id)
     }*/

}