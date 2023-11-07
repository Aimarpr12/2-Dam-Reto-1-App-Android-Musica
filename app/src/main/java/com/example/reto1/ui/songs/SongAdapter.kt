package com.example.reto1.ui.songs
import android.app.AlertDialog
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.reto1.R
import com.example.reto1.data.Song
import com.example.reto1.databinding.ItemSongsBinding
import com.squareup.picasso.Picasso

class SongAdapter (
    private val onYTListener: (String) -> Unit,
    private val onClickListener: (Song) -> Unit,
    private val onFavoriteDeleteClickListener: (Song) -> Unit,
    private val onFavoriteClickAddListener: (Song) -> Unit,
) : ListAdapter<Song, SongAdapter.SongViewHolder>(SongDiffCallback()){

    var selectedPosition: Int = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val binding = ItemSongsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SongViewHolder(binding)
    }
    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = getItem(position)
        holder.bind(song)
        holder.itemView.setOnClickListener {
            onClickListener(song)
        }
        if (position == selectedPosition) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.selectedRowColor))
            song.selected = true
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
            song.selected = false
        }
        holder.itemView.setOnClickListener {
            if (selectedPosition != position) {
                val previousSelectedPosition = selectedPosition
                selectedPosition = position
                notifyItemChanged(previousSelectedPosition)
                notifyItemChanged(position)

                onClickListener(song)
            }
        }

    }

    inner class SongViewHolder(private val binding: ItemSongsBinding) :

        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            binding.TextViewName.text = song.title
            binding.TextViewAuthor.text = song.author
            binding.id.text = song.id.toString()
            val videoUrl = song.url // URL del video de YouTube
            val videoId = videoUrl.substring(videoUrl.indexOf("=") + 1) // Extraer el ID del video
            val thumbnailUrl = "https://img.youtube.com/vi/$videoId/0.jpg"
            Picasso
                .get()
                .load(thumbnailUrl)
                .into(binding.imageButtonYT)

            binding.imageButtonYT.setOnClickListener {
                onYTListener(videoUrl)
            }

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