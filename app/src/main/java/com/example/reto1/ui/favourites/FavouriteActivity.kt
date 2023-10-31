package com.example.reto1.ui.favourites

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.reto1.R
import com.example.reto1.data.Song
import com.example.reto1.data.repository.remote.RemoteFavoritesDataSource
import com.example.reto1.databinding.FavouritesActivityBinding
import com.example.reto1.utils.Resource

class FavouriteActivity : ComponentActivity() {

    private lateinit var favouriteAdapter: FavouriteAdapter
    private var selectedSong: Song? = null
    private val favouriteRepository = RemoteFavoritesDataSource()

    private val viewModel: FavouriteViewModel by viewModels { FavouritesViewModelFactory(
        favouriteRepository
    )}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.favourites_activity)

        val binding = FavouritesActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(binding.root)
        setContentView(binding.root)

        favouriteAdapter = FavouriteAdapter(viewModel::onFavoriteViewHolderClick, viewModel::onFavoriteDeleteClick)
        binding.listViewFavourites.adapter = favouriteAdapter
        viewModel.items.observe(this, Observer {
            // esto es lo que se ejecuta cada vez que la lista en el VM cambia de valor
            when (it.status){
                Resource.Status.SUCCESS -> {
                    if(!it.data.isNullOrEmpty()){
                        favouriteAdapter.submitList(it.data)
                        findViewById<TextView>(R.id.textFavoritos).text = "Favoritos: " + it.data.size.toString()
                    }else{
                        favouriteAdapter.submitList(null)
                        findViewById<TextView>(R.id.textFavoritos).text = "Favoritos: 0"
                    }
                }
                Resource.Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
                Resource.Status.LOADING -> {

                }
            }
        })

        viewModel.deleted.observe(this, Observer {
            when (it.status){
                Resource.Status.SUCCESS -> {
                    Toast.makeText(this, "La canción " + it.data?.title + " ha sido eliminada de favoritos", Toast.LENGTH_LONG).show()
                }
                Resource.Status.ERROR -> {
                    Toast.makeText(this, "La canción " + it.data?.title +" no sido eliminada de favoritos", Toast.LENGTH_LONG).show()
                }
                Resource.Status.LOADING -> {

                }
            }
        })

        findViewById<Button>(R.id.buttonFiltros).setOnClickListener {
            findViewById<Button>(R.id.buttonFiltros).setOnClickListener {
                val builder = AlertDialog.Builder(this)

                val inflater = layoutInflater
                val dialogView = inflater.inflate(R.layout.popup_filtro_layout, null)

                dialogView.findViewById<TextView>(R.id.textView1).text = "Autor"
                dialogView.findViewById<TextView>(R.id.textView2).text = "Cancion"

                builder.setView(dialogView)

                builder.setPositiveButton("Aceptar") { _, _ ->
                    if(!dialogView.findViewById<EditText>(R.id.editTextAutor).text.toString().isNullOrBlank() || !dialogView.findViewById<EditText>(R.id.editTextCancion).text.toString().isNullOrBlank()) {
                        var autor = ""
                        var cancion = ""

                        if(!dialogView.findViewById<EditText>(R.id.editTextAutor).text.toString().isNullOrBlank() && dialogView.findViewById<EditText>(R.id.editTextCancion).text.toString().isNullOrBlank()){
                            autor = dialogView.findViewById<EditText>(R.id.editTextAutor).text.toString()
                        }else if(dialogView.findViewById<EditText>(R.id.editTextAutor).text.toString().isNullOrBlank() && !dialogView.findViewById<EditText>(R.id.editTextCancion).text.toString().isNullOrBlank()){
                            cancion = dialogView.findViewById<EditText>(R.id.editTextCancion).text.toString()
                        }else{
                            autor = dialogView.findViewById<EditText>(R.id.editTextAutor).text.toString()
                            cancion = dialogView.findViewById<EditText>(R.id.editTextCancion).text.toString()
                        }
                        Log.e("antes de pasar view model", autor + " " + cancion)
                        viewModel.filtrodeFavouriteList(autor, cancion)
                    }
                }

                builder.setNegativeButton("Cancelar") { _, _ ->

                }
                val dialog = builder.create()
                dialog.show()
            }

        }
    }


}