package com.example.playlistmaker.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.data.OnTrackClickListener

class FavoriteTrackAdapter(private var tracks: MutableList<Track>?,
                           private val listener: OnTrackClickListener
                      // тоже добавили листенер в конструктор класса
                   ) : RecyclerView.Adapter<TrackViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newTracks: MutableList<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int)  {
        holder.itemView.setOnClickListener {
            listener.onTrackClicked(tracks!![position])
        }
        holder.bind(tracks!![position])

    }

    override fun getItemCount(): Int {
        return tracks!!.size
    }



}




