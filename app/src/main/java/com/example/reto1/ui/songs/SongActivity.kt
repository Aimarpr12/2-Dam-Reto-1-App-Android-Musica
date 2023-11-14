package com.example.reto1.ui.songs


import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels

import androidx.lifecycle.Observer
import com.example.reto1.MyApp
import com.example.reto1.R
import com.example.reto1.data.Song
import com.example.reto1.data.repository.remote.RemoteFavoritesDataSource
import com.example.reto1.data.repository.remote.RemoteSongsDataSource
import com.example.reto1.databinding.SongsActivityBinding
import com.example.reto1.ui.favourites.FavouriteActivity
import com.example.reto1.ui.favourites.FavouriteViewModel
import com.example.reto1.ui.favourites.FavouritesViewModelFactory
import com.example.reto1.ui.users.ChangePasswordActivity
import com.example.reto1.ui.users.LoginActivity
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

    private val viewModelUser: SongsViewModel by viewModels {
        SongsViewModelFactory(songRepository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.songs_activity)
        //sacar el id de user
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

                        findViewById<TextView>(R.id.songs).text =
                            "Songs: " + it.data.size.toString()
                    }

                else{

                    songAdapter.submitList(null)
                    findViewById<TextView>(R.id.songs).text = "Favoritos: 0"
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
                    viewModel.updateSongList()
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
                    viewModel.updateSongList();
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
                    viewModel.updateSongList();
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
                    viewModel.updateSongList();
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

        /*binding.addSong.setOnClickListener() {

            binding.addList.setOnClickListener() {
                viewModel.onAddSong(
                    binding.newSongUrl.text.toString(),
                    binding.newSongTitle.text.toString(),
                    binding.newSongAuthor.text.toString()
                )
            }
        }*/
        binding.filterSong.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.popup_filtro_layout, null)

            dialogView.findViewById<TextView>(R.id.textViewAutor).text = "Autor"
            dialogView.findViewById<TextView>(R.id.textViewCancion).text = "Cancion"

            builder.setView(dialogView)

            builder.setPositiveButton("Aceptar") { _, _ ->

                val autor = dialogView.findViewById<EditText>(R.id.editTextAutor).text.toString()
                val cancion = dialogView.findViewById<EditText>(R.id.editTextCancion).text.toString()

                viewModel.filtrodeSongList(autor, cancion)
            }
            builder.setNegativeButton("Cancelar") { _, _ ->

            }
            val dialog = builder.create()
            dialog.show()
        }

        binding.buttonChangePass?.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.deleteSong.setOnClickListener {

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
        binding.buttonGoToFav.setOnClickListener {
            val intent = Intent(this, FavouriteActivity::class.java).apply {
                // putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
            finish()
        }

        binding.buttonLogOut.setOnClickListener {
            MyApp.userPreferences.logOut()
            val intent = Intent(this, LoginActivity::class.java).apply {
                // putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
            finish()
        }
        binding.addSong.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.popup_add_song, null)
            builder.setView(dialogView)

            builder.setPositiveButton("Aceptar") { _, _ ->

                val url = dialogView.findViewById<EditText>(R.id.editTextSongUrl).text.toString()
                val autor = dialogView.findViewById<EditText>(R.id.editTextSongAuthor).text.toString()
                val cancion = dialogView.findViewById<EditText>(R.id.editTextSongTitle).text.toString()

                viewModel.onAddSong(
                    url,
                    autor,
                    cancion
                )

            }
            builder.setNegativeButton("Cancelar") { _, _ ->

            }
            val dialog = builder.create()
            dialog.show()
        }

        binding.nameUser.text = MyApp.userPreferences.fetchLogin()
    }

    private fun onYTListener(url: String) {
        val webIntent: Intent = Uri.parse(url).let { webpage ->
            Intent(Intent.ACTION_VIEW, webpage)
        }
        startActivity(webIntent)
    }
}

