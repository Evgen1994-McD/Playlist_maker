package com.example.playlistmaker.ui.player.fragments

import com.example.playlistmaker.ui.media.onPlaylistClickListener


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


class PlayListViewHolder(itemView: View, listener: onPlaylistClickListener) :
    RecyclerView.ViewHolder(itemView) { // Добавили листенер в конструктор класса

    private val playListName: TextView = itemView.findViewById(R.id.album_name)
    private val playListSize: TextView = itemView.findViewById(R.id.album_info)
    private val playListImage: ImageView = itemView.findViewById(R.id.album_Image)
    private val options = RequestOptions().centerCrop()
    private val radiusInDP = 2f
    private val densityMultiplier = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        1f,
        itemView.context.resources.displayMetrics
    )
    private val radiusInPX = radiusInDP * densityMultiplier





    @SuppressLint("CheckResult")
    fun bind(playList: PlayList) {
        playListName.text = playList.name
        playListSize.text = playList.size.toString()


        Glide.with(itemView.context)
            .load(playList.image?.toUri())  //У меня там просто имя.jpg - это не ссылка, переделать
            .transform(RoundedCorners(radiusInPX.toInt()))
            .apply(options)
            .placeholder(R.drawable.ic_placeholder_45)
            .error(R.drawable.ic_placeholder_45)
            .into(playListImage)


    }


}