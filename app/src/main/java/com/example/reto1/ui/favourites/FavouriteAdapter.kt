package com.example.reto1.ui.favourites

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
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


class FavouriteAdapter(
    private val onYTListener: (String) -> Unit,
    private val onClickListener: (Song) -> Unit,
    private val onFavoriteClickListener: (Song) -> Unit,
) : ListAdapter<Song, FavouriteAdapter.FavouriteViewHolder>(FavouriteDiffCallback()) {

    var selectedPosition: Int = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val binding = ItemSongsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val song = getItem(position)
        holder.bind(song)
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

    inner class FavouriteViewHolder(private val binding: ItemSongsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(song: Song) {
            binding.TextViewName.text = song.title.replace(Regex("\\(.*?\\)"), "")
            binding.TextViewAuthor.text = song.author
            val videoUrl = song.url // URL del video de YouTube
            val videoId = videoUrl.substring(videoUrl.indexOf("=") + 1) // Extraer el ID del video
            val thumbnailUrl = "https://img.youtube.com/vi/$videoId/0.jpg"
            Picasso
                .get()
                .load(thumbnailUrl)
                .into(binding.imageButtonYT)

            binding.imageButtonFav.setBackgroundColor(Color.TRANSPARENT)
            val layoutParams = binding.imageButtonFav.layoutParams
            val nuevoAncho = 230
            val nuevoAlto = 230
            layoutParams.width = nuevoAncho
            layoutParams.height = nuevoAlto
            binding.imageButtonFav.layoutParams = layoutParams

            binding.imageButtonYT.setOnClickListener {
                onYTListener(videoUrl)
            }
            binding.imageButtonFav.setOnClickListener {
                val builder = AlertDialog.Builder(binding.root.context)
                builder.setTitle("Confirmación")
                val title = song.title
                val message = "¿Estás seguro de que deseas eliminar la canción de <b>$title</b> de favoritos?"
                val spannableMessage = SpannableStringBuilder(Html.fromHtml(message))
                spannableMessage.setSpan(StyleSpan(Typeface.BOLD), 50, 40 + title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                builder.setMessage(spannableMessage)

                builder.setPositiveButton("Sí") { _, _ ->
                    onFavoriteClickListener(song)
                }

                builder.setNegativeButton("No") { _, _ ->

                }

                val dialog = builder.create()
                dialog.show()
            }

        }
    }
    class FavouriteDiffCallback : DiffUtil.ItemCallback<Song>() {

        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            return (oldItem.id == newItem.id && oldItem.author == newItem.author && oldItem.title == newItem.title && oldItem.url == newItem.url)
        }

    }
}


