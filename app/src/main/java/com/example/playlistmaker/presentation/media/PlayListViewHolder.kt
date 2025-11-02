package com.example.playlistmaker.presentation.media

import android.annotation.SuppressLint
import android.util.TypedValue
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlayListItemBinding
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.utils.declineNoun


class PlayListViewHolder(private val binding: PlayListItemBinding, listener: onPlaylistClickListener) :
    RecyclerView.ViewHolder(binding.root) { // Добавили листенер в конструктор класса

   private val playListName = binding.tvName
   private val playListSize = binding.tvSize
   private val playListImage=binding.imPlaylistImage
    private val radiusInDP = 8f
    private val radiusInPX = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        radiusInDP,
        itemView.context.resources.displayMetrics
    )
    private val options = RequestOptions()
        .centerCrop()
        .transform(RoundedCorners(radiusInPX.toInt()))







    @SuppressLint("CheckResult")
    fun bind(playList: PlayList) {
        playListName.text = playList.name

        val oneForm = binding.tvSize.context.getString(R.string.track1)
        val twoForm = binding.tvSize.context.getString(R.string.track3)
        val fiveAndMoreForm = binding.tvSize.context.getString(R.string.track2)

        playListSize.text = declineNoun(playList.size, oneForm, twoForm, fiveAndMoreForm)





    Glide.with(playListImage.context)
        .load(playList.image?.toUri())
        .apply(options)
        .placeholder(R.drawable.ic_placeholder_45)
        .error(R.drawable.ic_placeholder_45)
        .into(playListImage)


    }


}