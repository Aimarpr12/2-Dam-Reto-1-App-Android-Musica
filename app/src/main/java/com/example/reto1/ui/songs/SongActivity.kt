package com.example.reto1.ui.songs

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.reto1.R
import com.example.reto1.data.Song
import com.example.reto1.data.repository.remote.RemoteFavoritesDataSource
import com.example.reto1.data.repository.remote.RemoteSongsDataSource
import com.example.reto1.databinding.SongsActivityBinding
import com.example.reto1.ui.favourites.FavouriteViewModel
import com.example.reto1.ui.favourites.FavouritesViewModelFactory
import com.example.reto1.utils.Resource

fun onSongsListClickItem(song: Song) {
    Log.i("PRUEBA1", "va2")
    Log.i("PRUEBA1", song.title)

}

class SongActivity: ComponentActivity() {

    private lateinit var songAdapter: SongAdapter
    private val songRepository = RemoteSongsDataSource();
    private val favouriteRepository = RemoteFavoritesDataSource()
    private val viewModel: SongsViewModel by viewModels {
        SongsViewModelFactory(songRepository)
    }
    private val viewModelFav: FavouriteViewModel by viewModels {
        FavouritesViewModelFactory(favouriteRepository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.songs_activity)
        val binding = SongsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        songAdapter = SongAdapter(::onSongsListClickItem, viewModelFav::onFavoriteDeleteClick, viewModelFav::onFavoriteAddClick)
        // a la lista de empleados le incluyo el adapter de empleado
        binding.favouriteSongsList.adapter = songAdapter

        viewModel.items.observe(this, Observer {

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

                }
            }
        })
        viewModel.created.observe(this, Observer {
            when (it.status) {

                Resource.Status.SUCCESS -> {
                    viewModel.updateSongList(1)

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

        viewModelFav.deleted.observe(this, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Toast.makeText(
                        this,
                        "La canción " + it.data?.title + " ha sido eliminada de favoritos",
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.updateSongList(1);
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(
                        this,
                        "La canción " + it.data?.title + " no sido eliminada de favoritos",
                        Toast.LENGTH_LONG
                    ).show()
                }

                Resource.Status.LOADING -> {

                }
            }
        })

        viewModelFav.created.observe(this, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Toast.makeText(
                        this,
                        "La canción " + it.data?.title + " ha sido añadida de favoritos",
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.updateSongList(1);
                }

                Resource.Status.ERROR -> {
                    Toast.makeText(
                        this,
                        "La canción " + it.data?.title + " no sido añadida de favoritos",
                        Toast.LENGTH_LONG
                    ).show()
                }

                Resource.Status.LOADING -> {

                }
            }
        })

        binding.addSong.setOnClickListener() {
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
        binding.seachSong.setOnClickListener {
            binding.addSong.visibility = View.GONE
            binding.deleteSong.visibility = View.GONE
            binding.newSongId.visibility = View.VISIBLE


        }
    }
}

