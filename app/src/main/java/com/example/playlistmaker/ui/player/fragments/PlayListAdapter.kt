package com.example.playlistmaker.ui.player.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.ui.media.onPlaylistClickListener

class PlayListAdapter(
    private val listener: onPlaylistClickListener,
    private val playlistDiffCallback: PlaylistDiffCallback
) : ListAdapter<PlayList, PlayListViewHolder>(playlistDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item_on_player, parent, false)
        return PlayListViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(getItem(position)) // Используем getItem(), который приходит от ListAdapter
        holder.itemView.setOnClickListener {
            listener.onPlaylistClicked(getItem(position))
        }

    }

    fun submitNewList(newList: List<PlayList>) {
        submitList(newList)
    }
}

class PlaylistDiffCallback : DiffUtil.ItemCallback<PlayList>() {
    override fun areItemsTheSame(oldItem: PlayList, newItem: PlayList): Boolean {
        return oldItem.listId == newItem.listId || oldItem.tracksId == newItem.tracksId
    }

    override fun areContentsTheSame(oldItem: PlayList, newItem: PlayList): Boolean {
        return oldItem == newItem || oldItem.tracksId == newItem.tracksId
    }
}



