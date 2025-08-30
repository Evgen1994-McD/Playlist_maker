package com.example.playlistmaker.ui.media

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.adapters.TrackViewHolder
import com.example.playlistmaker.ui.search.listener.OnTrackClickListener

class PlaylistTrackAdapter(
    private var tracks: List<Track>?,
    private val listener: OnTrackClickListener,
    private val longClickListener: OnItemLongClickListener
) : RecyclerView.Adapter<PlaylistTrackViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return PlaylistTrackViewHolder(view, listener, longClickListener)
    }

    override fun onBindViewHolder(holder: PlaylistTrackViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            listener.onTrackClicked(tracks!![position])
        }

        holder.itemView.setOnLongClickListener{
        longClickListener.onItemLongClick(tracks!![position])
            true
        }



        holder.bind(tracks!![position])
    }

    override fun getItemCount(): Int {
        return tracks!!.size
    }

}




