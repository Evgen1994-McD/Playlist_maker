package com.example.playlistmaker.presentation.viewModels

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.App
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.api.FavoriteTrackInteractor
import com.example.playlistmaker.domain.api.MediaInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.models.MediaPlayerCommand

class MediaViewModel(private val favoriteTrackInteractor: FavoriteTrackInteractor,
                     private val mediaInteractor: MediaInteractor,
                     private val application: Application,
                     private val intent: Intent
) : AndroidViewModel(application){

    companion object { // компаньон медиаплеера

        private const val default_time = "00:00" // для прогресса
    }

private val mutableMediaScreen = MutableLiveData(Track())
    private val handler = Handler(Looper.getMainLooper()) // хэндлер для доступа к главному потоку




    fun mediaCommander(command: MediaPlayerCommand){
        when(command){
            is MediaPlayerCommand.Play -> mediaInteractor.startPlayback()
            is MediaPlayerCommand.Pause -> mediaInteractor.pausePlayback()
            is MediaPlayerCommand.SeekTo -> handler.postDelayed({mediaInteractor.updateProgress()} , 300L) // обновляем прогресс
        }
    }

    fun intentGetExtraBind() {
        val intent = intent // получаем интент который запустил активность
        val trackName = intent.getStringExtra("trackName")
       val previewUrl = intent.getStringExtra("previewUrl").toString()

        val trackTimeMillis = intent.getStringExtra("trackTimeMillis")
        val artistName = intent.getStringExtra("artistName")
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")
        val relieseDate = intent.getStringExtra("relieseDate")
       val artworkUrl100 = intent.getStringExtra("artworkUrl100").toString()

        if (intent.getStringExtra("collectionName")
                ?.isNullOrEmpty() == true || intent.getStringExtra("collectionName")
                ?.contains("No Album") == true // Если нет альбома или ответ сервера содержит No Album то убираем поле с альбомом
        ) {
//            binding.tvAlbum.makeGone()// убираем поле альбом если нет альбома
//            binding.tvAlbumLeft.makeGone()// убираем поле альбом если нет альбома

        } else {
            val collectionName = intent.getStringExtra("collectionName")
                .toString()// убираем поле альбом если нет альбома
//            binding.tvAlbum.makeVisible()// убираем поле альбом если нет альбома
//            binding.tvAlbum.text = collectionName// убираем поле альбом если нет альбома
        }
//        binding.tvGenre.text = primaryGenreName
//        binding.tvCountry.text = country
//        binding.tvAlbum.text = collectionName
//        binding.tvTime.text = trackTimeMillis
//        binding.tvYear.text = relieseDate
//
//        binding.tvGroup.text = artistName
//        binding.tvTrackName.text = trackName


//        val options = RequestOptions().centerCrop()//опции для Glide
//
//        Glide.with(binding.imMine.context).load(artworkUrl100).apply(options)
//            .placeholder(R.drawable.ph_media_312).error(R.drawable.ph_media_312)
//            .into(binding.imMine)
//        Log.d("MediaActivity", "Preview URL: $previewUrl")
        mediaInteractor.preparePlayer(previewUrl)

    }





        class CustomViewModelFactory(
            private val favoriteInteractor: FavoriteTrackInteractor,
            private val mediaInteractor: MediaInteractor,
           private val application: Application,
            private val intent : Intent
        ) : ViewModelProvider.NewInstanceFactory() {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return when {
                    modelClass.isAssignableFrom(MediaViewModel::class.java) -> MediaViewModel(favoriteInteractor, mediaInteractor, application, intent  ) as T
                    else -> throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }

    }