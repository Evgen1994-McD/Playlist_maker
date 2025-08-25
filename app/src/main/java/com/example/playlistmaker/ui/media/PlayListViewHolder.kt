package com.example.playlistmaker.ui.media

import android.annotation.SuppressLint
import android.os.Environment
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.listener.OnTrackClickListener
import com.example.playlistmaker.utils.Constants
import java.io.File


class PlayListViewHolder(itemView: View, listener: onPlaylistClickListener) :
    RecyclerView.ViewHolder(itemView) { // Добавили листенер в конструктор класса

   private val playListName: TextView = itemView.findViewById(R.id.tv_name)
   private val playListSize: TextView = itemView.findViewById(R.id.tv_size)
   private val playListImage: ImageView = itemView.findViewById(R.id.im_playlist_image)
    private val options = RequestOptions().centerCrop()
    private val radiusInDP = 650f
    private val radiusInPX = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        radiusInDP,
        itemView.context.resources.displayMetrics
    )
//    private val radiusInPX = radiusInDP * densityMultiplier





    @SuppressLint("CheckResult")
    fun bind(playList: PlayList) {
        playListName.text = playList.name
        playListSize.text = playList.size.toString()+" треков"


    Glide.with(itemView.context)
        .load(playList.image?.toUri())  //У меня там просто имя.jpg - это не ссылка, переделать
        .transform(RoundedCorners(600))
        .apply(options)
        .placeholder(R.drawable.ic_placeholder_45)
        .error(R.drawable.ic_placeholder_45)
        .into(playListImage)


    }


}