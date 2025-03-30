package com.example.playlistmaker.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.retrofit.Track
import com.example.playlistmaker.utils.OnTrackClickListener

class FavoriteTrackAdapter(private var tracks: MutableList<Track>?,
    private val listener: OnTrackClickListener
                      // тоже добавили листенер в конструктор класса
                   ) : RecyclerView.Adapter<FavoriteTrackViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newTracks: MutableList<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTrackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return FavoriteTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteTrackViewHolder, position: Int)  {
        holder.itemView.setOnClickListener {
            listener.onTrackClicked(tracks!![position])
        }
        holder.bind(tracks!![position])

    }

    override fun getItemCount(): Int {
        return tracks!!.size
    }



}




