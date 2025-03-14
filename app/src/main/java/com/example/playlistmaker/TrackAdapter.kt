package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.retrofit.Track
import com.example.playlistmaker.utils.OnTrackClickListener

class TrackAdapter(private val tracks: List<Track>?,
                   private val listener: OnTrackClickListener  // тоже добавили листенер в конструктор класса
                   ) : RecyclerView.Adapter<TrackViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TrackViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int)  {
        holder.bind(tracks!![position], listener)

    }

    override fun getItemCount(): Int {
        return tracks!!.size
    }

}




