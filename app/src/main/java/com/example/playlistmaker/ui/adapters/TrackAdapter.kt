package com.example.playlistmaker.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.data.OnTrackClickListener

class TrackAdapter(private val tracks: List<Track>?,
                   private val listener: OnTrackClickListener  // тоже добавили листенер в конструктор класса
                   ) : RecyclerView.Adapter<TrackViewHolder>() {




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




