package com.example.reto1.ui.songs

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.example.reto1.data.Song
import androidx.lifecycle.Observer
import com.example.reto1.data.repository.remote.RemoteSongsDataSource

import com.example.reto1.databinding.SongsActivityBinding
import com.example.reto1.utils.Resource
import com.example.reto1.ui.songs.SongAdapter

fun onSongsListClickItem(song: Song) {
    Log.i("PRUEBA1", "va2")
    Log.i("PRUEBA1", song.title)

}

class SongActivity: ComponentActivity() {

    private lateinit var songAdapter: SongAdapter
    private val songRepository = RemoteSongsDataSource();
    private val viewModel: SongsViewModel by viewModels {
        SongsViewModelFactory(songRepository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = SongsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        songAdapter = SongAdapter(::onSongsListClickItem)
        // a la lista de empleados le incluyo el adapter de empleado
        binding.songsList.adapter = songAdapter

        viewModel.items.observe(this, Observer {
            // esto es lo que se ejecuta cada vez que la lista en el VM cambia de valor
            Log.e("PruebasDia1", "ha ocurrido un cambio en la lista")

            when (it.status) {
                Resource.Status.SUCCESS -> {
                    if (!it.data.isNullOrEmpty()) {
                        songAdapter.submitList(it.data)
                    }
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }

                Resource.Status.LOADING -> {
                    // de momento
                }
            }

            //
        })
        viewModel.created.observe(this, Observer {
            when (it.status) {

                Resource.Status.SUCCESS -> {
                    viewModel.updateSongList()

                    binding.seachSong.visibility = View.VISIBLE
                    binding.deleteSong.visibility = View.VISIBLE
                    binding.newSongUrl.visibility = View.GONE
                    binding.newSongTitle.visibility = View.GONE
                    binding.newSongAuthor.visibility = View.GONE
                    binding.addList.visibility = View.GONE
                }
                Resource.Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                Resource.Status.LOADING -> {
                    // de momento
                }
            }

        })
        binding.addSong.setOnClickListener () {
            binding.seachSong.visibility = View.GONE
            binding.deleteSong.visibility = View.GONE
            binding.newSongUrl.visibility = View.VISIBLE
            binding.newSongTitle.visibility = View.VISIBLE
            binding.newSongAuthor.visibility = View.VISIBLE
            binding.addList.visibility = View.VISIBLE

            binding.addList.setOnClickListener() {
                viewModel.onAddEmployee(
                    binding.newSongUrl.text.toString(),
                    binding.newSongTitle.text.toString(),
                    binding.newSongAuthor.text.toString()
                )
            }
        }
        binding.seachSong.setOnClickListener{
            binding.addSong.visibility = View.GONE
            binding.deleteSong.visibility = View.GONE
            binding.newSongId.visibility = View.VISIBLE


        }


    }

}

