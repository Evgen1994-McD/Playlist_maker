package com.example.playlistmaker.ui.media

import android.annotation.SuppressLint
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.listener.OnTrackClickListener


class PlaylistTrackViewHolder(itemView: View,
                              listener: OnTrackClickListener,
                              longClickListener: OnItemLongClickListener) :
    RecyclerView.ViewHolder(itemView) { // Добавили листенер в конструктор класса

    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.track_info)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val trackImage: ImageView = itemView.findViewById(R.id.track_image)
    private val options = RequestOptions().centerCrop()
    private val radiusInDP = 2f
    private val densityMultiplier = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        1f,
        itemView.context.resources.displayMetrics
    )
    private val radiusInPX = radiusInDP * densityMultiplier





    @SuppressLint("CheckResult")
    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text =
            track.trackTimeMillis// formatMillisecondsAsMinSec(track.trackTimeMillis.toLong())


        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(radiusInPX.toInt()))
            .apply(options)
            .placeholder(R.drawable.ic_placeholder_45)
            .error(R.drawable.ic_placeholder_45)
            .into(trackImage)


    }

}
