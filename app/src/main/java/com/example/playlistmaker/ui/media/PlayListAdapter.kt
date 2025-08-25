package com.example.playlistmaker.ui.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.PlayList


class PlayListAdapter(
    private var playLists: List<PlayList>?,
    private val listener: onPlaylistClickListener  // тоже добавили листенер в конструктор класса
) : RecyclerView.Adapter<PlayListViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.play_list_item, parent, false)
        return PlayListViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener.onPlaylistClicked(playLists!![position])
        }

        holder.bind(playLists!![position])
    }

    override fun getItemCount(): Int {
        return playLists!!.size
    }

}
