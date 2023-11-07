package com.example.reto1.ui.songs


import android.content.Intent
import android.net.Uri
import android.os.Bundle
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

private var selectedSong : Song = Song()
fun onSongsListClickItem(song: Song) {
    selectedSong = song
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

        songAdapter = SongAdapter(
            ::onYTListener,
            ::onSongsListClickItem,
            viewModelFav::onFavoriteDeleteClick,
            viewModelFav::onFavoriteAddClick
        )
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

                    binding.filterSong.visibility = View.VISIBLE
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
        viewModel.deleted.observe(this, Observer {
            when (it.status){
                Resource.Status.SUCCESS -> {

                    Toast.makeText(this, "La canción " + viewModel.deletedName + " ha sido eliminada de favoritos", Toast.LENGTH_LONG).show()
                    viewModel.updateSongList(1);
                }
                Resource.Status.ERROR -> {
                    Toast.makeText(this, "La canción " + viewModel.deletedName +" no sido eliminada de favoritos", Toast.LENGTH_LONG).show()
                }
                Resource.Status.LOADING -> {

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
            binding.filterSong.visibility = View.GONE
            binding.deleteSong.visibility = View.GONE
            binding.newSongUrl.visibility = View.VISIBLE
            binding.newSongTitle.visibility = View.VISIBLE
            binding.newSongAuthor.visibility = View.VISIBLE
            binding.addList.visibility = View.VISIBLE

            binding.addList.setOnClickListener() {
                viewModel.onAddSong(
                    binding.newSongUrl.text.toString(),
                    binding.newSongTitle.text.toString(),
                    binding.newSongAuthor.text.toString()
                )
            }
        }
        binding.filterSong.setOnClickListener {


        }
        binding.deleteSong.setOnClickListener {
            /*binding.addList.visibility = View.GONE
            binding.filterSong.visibility = View.GONE
            binding.deleteSong.visibility = View.GONE
            binding.newSongTitle.visibility = View.VISIBLE
            binding.deleteList.visibility = View.VISIBLE
            binding.deleteList.setOnClickListener() {
                viewModel.onDeleteSong(
                    binding.newSongTitle.text.toString(),
                )

            }*/

            if(selectedSong.id != 0){
                viewModel.onDeleteSong(
                    selectedSong,
                )
            }else{
                Toast.makeText(
                    this,
                    "Selecciona una cancion para eliminar",
                    Toast.LENGTH_LONG
                ).show()
            }


        }
    }

    private fun onYTListener(url: String) {
        val webIntent: Intent = Uri.parse(url).let { webpage ->
            Intent(Intent.ACTION_VIEW, webpage)
        }
        startActivity(webIntent)
    }
}

