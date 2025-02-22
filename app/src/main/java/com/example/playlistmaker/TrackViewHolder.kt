package com.example.playlistmaker
import android.content.res.Resources
import android.util.TypedValue
import android.view.RoundedCorner
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions


class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val trackName : TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.track_info)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val trackImage : ImageView = itemView.findViewById(R.id.trackImage)
    private val options = RequestOptions().centerCrop()
    private val radiusInDP = 2f
    val densityMultiplier = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, Resources.getSystem().displayMetrics)
    val radiusInPX = radiusInDP * densityMultiplier



    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTime
        options.centerCrop()
        Glide.with(itemView.context)
            .load(track.artworkUrl100)
            .transform(RoundedCorners(radiusInPX.toInt()))
            .apply { options }
            .into(trackImage)



    }
}
