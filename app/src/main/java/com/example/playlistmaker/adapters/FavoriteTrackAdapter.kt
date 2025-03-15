package com.example.playlistmaker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.retrofit.Track

class FavoriteTrackAdapter(private val tracks: MutableList<Track>?,
                      // тоже добавили листенер в конструктор класса
                   ) : RecyclerView.Adapter<FavoriteTrackViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return FavoriteTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteTrackViewHolder, position: Int)  {
        holder.bind(tracks!![position])

    }

    override fun getItemCount(): Int {
        return tracks!!.size
    }

}




