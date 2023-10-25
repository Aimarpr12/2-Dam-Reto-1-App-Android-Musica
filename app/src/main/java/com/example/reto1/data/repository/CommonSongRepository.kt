package com.example.reto1.data.repository

import com.example.reto1.data.Song
import com.example.reto1.utils.Resource

interface CommonSongRepository {
        suspend fun getSongs(): Resource<List<Song>>
        suspend fun createSong(song: Song): Resource<Integer>

}