package com.example.playlistmaker.presentation.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.PlayListItemBinding
import com.example.playlistmaker.domain.models.PlayList


class PlayListAdapter(
    private var playLists: List<PlayList>?,
    private val listener: onPlaylistClickListener  // тоже добавили листенер в конструктор класса
) : RecyclerView.Adapter<PlayListViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PlayListItemBinding.inflate(inflater, parent, false)

        return PlayListViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val playlist = playLists?.get(position)
            playlist?.let { listener.onPlaylistClicked(it) }
        }
        val playlist = playLists?.get(position)
playlist?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return playLists!!.size
    }

}
