package com.example.reto1.ui.songs
import android.app.AlertDialog
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reto1.R
import com.example.reto1.data.Song
import com.example.reto1.databinding.ItemSongBinding

class SongAdapter (
    private val onClickListener: (Song) -> Unit,
    private val onFavoriteDeleteClickListener: (Song) -> Unit,
    private val onFavoriteClickAddListener: (Song) -> Unit,
) : ListAdapter<Song, SongAdapter.SongViewHolder>(SongDiffCallback()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemSongBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }
    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = getItem(position)
        holder.bind(song)
        holder.itemView.setOnClickListener {
            onClickListener(song)
            Log.e("aada", song.toString())
        }
    }

    inner class SongViewHolder(private val binding: ItemSongBinding) :

        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            Log.e("aada", song.toString())
            binding.textViewTitle.text = song.url
            binding.textViewSubtitle1.text = song.title
            binding.textViewSubtitle2.text = song.author

            binding.imageButtonFav.setBackgroundColor(Color.TRANSPARENT)
            val layoutParams = binding.imageButtonFav.layoutParams
            val nuevoAncho = 230
            val nuevoAlto = 230
            layoutParams.width = nuevoAncho
            layoutParams.height = nuevoAlto
            binding.imageButtonFav.layoutParams = layoutParams
            if(song.favorite){
                binding.imageButtonFav.setImageResource(R.drawable.favorito_negro)
            }else{
                binding.imageButtonFav.setImageResource(R.drawable.favorito_blanco)
            }

            binding.imageButtonFav.setOnClickListener {
                if(song.favorite){
                    val builder = AlertDialog.Builder(binding.root.context)
                    builder.setTitle("Confirmación")
                    builder.setMessage("¿Estás seguro de que deseas eliminar la cancion de " + song.title +  "  de favoritos?")

                    builder.setPositiveButton("Sí") { _, _ ->
                        onFavoriteDeleteClickListener(song)
                    }

                    builder.setNegativeButton("No") { _, _ ->

                    }

                    val dialog = builder.create()
                    dialog.show()

                }else{
                    onFavoriteClickAddListener(song)
                }
            }
        }
    }
    class SongDiffCallback : DiffUtil.ItemCallback<Song>() {

        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem == newItem
        }

    }


}