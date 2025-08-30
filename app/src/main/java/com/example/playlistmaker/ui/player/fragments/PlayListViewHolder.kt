package com.example.playlistmaker.ui.player.fragments

import com.example.playlistmaker.ui.media.onPlaylistClickListener


import android.annotation.SuppressLint
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
import com.example.playlistmaker.utils.declineNoun
import java.io.File


class PlayListViewHolder(itemView: View,
                         listener: onPlaylistClickListener) :
    RecyclerView.ViewHolder(itemView) { // Добавили листенер в конструктор класса


    private val playListName: TextView = itemView.findViewById(R.id.album_name)
    private val playListSize: TextView = itemView.findViewById(R.id.album_info)
    private val playListImage: ImageView = itemView.findViewById(R.id.album_image)
    private val options = RequestOptions().centerCrop()
    private val radiusInDP = 8f
    private val radiusInPX = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        radiusInDP,
        itemView.context.resources.displayMetrics
    )





    @SuppressLint("CheckResult")
    fun bind(playList: PlayList) {
        val oneForm = itemView.context.getString(R.string.track1)
        val twoForm = itemView.context.getString(R.string.track3)
        val fiveAndMoreForm =itemView.context.getString(R.string.track2)

        playListSize.text = declineNoun(playList.size, oneForm, twoForm, fiveAndMoreForm)
        playListName.text = playList.name




        Glide.with(itemView.context)
            .load(playList.image?.toUri())
            .transform(RoundedCorners(radiusInPX.toInt()))
            .apply(options)
            .placeholder(R.drawable.ic_placeholder_45)
            .error(R.drawable.ic_placeholder_45)
            .into(playListImage)

    }
//    val options = RequestOptions().centerCrop()//опции для Glide
//    val radiusInDP = 8f
//    val radiusInPX = TypedValue.applyDimension(
//        TypedValue.COMPLEX_UNIT_DIP,
//        radiusInDP,
//        resources.displayMetrics
//    )
//    Glide.with(binding.imMine.context)
//    .load(newState.artworkUrl100)
//    .apply(options)
//    .placeholder(R.drawable.ph_media_312).error(R.drawable.ph_media_312)
//    .transform(RoundedCorners(radiusInPX.toInt()))
//    .into(binding.imMine)
}





