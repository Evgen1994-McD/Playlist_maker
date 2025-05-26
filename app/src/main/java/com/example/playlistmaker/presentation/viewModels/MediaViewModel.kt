package com.example.playlistmaker.presentation.viewModels

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
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
import com.example.playlistmaker.presentation.activity.MediaActivity
import com.example.playlistmaker.presentation.models.MediaPlayerCommand
import com.example.playlistmaker.presentation.models.MediaScreenState
import com.example.playlistmaker.presentation.models.SearchScreenState

class MediaViewModel(private val favoriteTrackInteractor: FavoriteTrackInteractor,
                     private val mediaInteractor: MediaInteractor,
                     private val application: Application,
                     private val intent: Intent
) : AndroidViewModel(application) {

    companion object { // компаньон медиаплеера
//        private var isPlaying = false // переменная статуса плеера

        private const val default_time = "00:00" // для прогресса
    }

    private val mutableMediaScreen = MutableLiveData(
        MediaScreenState(
            "", "" +
                    "", "", "", "", "", "", "", "", ""
        )
    ) // пустой трекдто

    val getLiveData: LiveData<MediaScreenState> get() = mutableMediaScreen


    private val handler = Handler(Looper.getMainLooper()) // хэндлер для доступа к главному потоку


    fun addListeners() {
        mediaInteractor.addListeners(
            ::onPlayerReady, ::onPlayComplete
        )  // листенер для определения начала и окончания воспроизведения

    }


    private fun onPlayerReady() { // это функция для листенера
//        binding.play.isEnabled = true
//        startUpdateProgress()
        startUpdateProgress()

    }

    private fun onPlayComplete() { // это тоже
//        stopUpdateProgress()
//        binding.pause.makeInvisible()
//        binding.play.makeVisible()
//        binding.progressTime.text = MediaActivity.Companion.default_time
        Log.d("MediaPlayer", "Проигрывание завершено")
        mutableMediaScreen.value = mutableMediaScreen.value!!.copy(progress = default_time, isPlaying = false)
        stopUpdateProgress()

    }

    fun stopUpdateProgress() {
//        mutableMediaScreen.value = mutableMediaScreen.value!!.copy(progress = default_time, isPlaying = false)
        handler.removeCallbacksAndMessages(null) // функция отмены колбеков от хендлер

    }

    @SuppressLint("SuspiciousIndentation")
    fun startUpdateProgress() {
//        if (!isPlaying) return
        val progress = mediaInteractor.updateProgress()
        mutableMediaScreen.value = mutableMediaScreen.value!!.copy(progress = progress)
        handler.postDelayed({ startUpdateProgress() }, 300) // вызывается каждые 300 мс
    }


    fun mediaCommander(command: MediaPlayerCommand) {
        when (command) {
            is MediaPlayerCommand.Play -> {
                mediaInteractor.startPlayback()
                mutableMediaScreen.value = mutableMediaScreen.value!!.copy(isPlaying = true)
                startUpdateProgress()

            }
            is MediaPlayerCommand.Pause -> {
                stopUpdateProgress()
            mediaInteractor.pausePlayback()
                mutableMediaScreen.value = mutableMediaScreen.value!!.copy(isPlaying = false)

            }

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
            mutableMediaScreen.value = mutableMediaScreen.value!!.copy(collectionName = "NoAlbum")


        } else {
            val collectionName = intent.getStringExtra("collectionName")
                .toString()// убираем поле альбом если нет альбома
            mutableMediaScreen.value =
                mutableMediaScreen.value!!.copy(collectionName = collectionName.toString())

            mediaInteractor.preparePlayer(previewUrl)
            mutableMediaScreen.value = mutableMediaScreen.value!!.copy(
                trackName = trackName.toString(),
                primaryGenreName = primaryGenreName.toString(),
                artistName = artistName.toString(),
                trackTimeMillis = trackTimeMillis.toString(),
                artworkUrl100 = artworkUrl100.toString(),
                releaseDate = relieseDate.toString(),
                country = country.toString()
            )


        }
    }



        class CustomViewModelFactory(
            private val favoriteInteractor: FavoriteTrackInteractor,
            private val mediaInteractor: MediaInteractor,
            private val application: Application,
            private val intent: Intent
        ) : ViewModelProvider.NewInstanceFactory() {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return when {
                    modelClass.isAssignableFrom(MediaViewModel::class.java) -> MediaViewModel(
                        favoriteInteractor,
                        mediaInteractor,
                        application,
                        intent
                    ) as T

                    else -> throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }

    }
