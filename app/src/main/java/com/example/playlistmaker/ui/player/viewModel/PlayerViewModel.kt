package com.example.playlistmaker.ui.player.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.db.MainDb
import com.example.playlistmaker.data.db.converters.TrackDbConvertor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.FavoriteTrackInteractor
import com.example.playlistmaker.domain.player.MediaInteractor
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class PlayerViewModel(private val favoriteTrackInteractor: FavoriteTrackInteractor,
                               private val mediaInteractor: MediaInteractor,
    private val trackFromArgs: Track,
    private val mainDb: MainDb,
    private val trackDbConvertor: TrackDbConvertor

) : ViewModel(){
    private var timerJob :Job? = null

    companion object { // компаньон медиаплеера
        private const val default_time = "00:00" // для прогресса
        private const val noAlbum = "No Album"

    }




    private val mutableMediaScreen = MutableLiveData(
        PlayerScreenState()
    )


    val getLiveData: LiveData<PlayerScreenState> get() = mutableMediaScreen






    fun addListeners() {
        mediaInteractor.addListeners(
            ::onPlayerReady, ::onPlayComplete
        )  // листенер для определения начала и окончания воспроизведения

    }


    fun reliesePlayer(){
        mediaInteractor.releasePlayer()
        stopUpdateProgress()

    }

    private fun onPlayerReady() { // это функция для листенера
        startUpdateProgress()
        mutableMediaScreen.value = mutableMediaScreen.value!!.copy(isPlaying = false)
    }

    private fun onPlayComplete() { // это тоже
        Log.d("MyLog", "Проигрывание завершено")
        stopUpdateProgress()

        mutableMediaScreen.value?.let {
            mutableMediaScreen.value = it.copy(progress = default_time, isPlaying = false)
        }
            addListeners()
            intentGetExtraBind()

    }

    fun stopUpdateProgress() {
timerJob?.cancel()
    }

    @SuppressLint("SuspiciousIndentation")
    fun startUpdateProgress() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            try {

                while (isActive && mutableMediaScreen.value!!.isPlaying) {
                    delay(300L)
                    ensureActive()
                    val progress = mediaInteractor.updateProgress()
                    mutableMediaScreen.postValue(mutableMediaScreen.value!!.copy(progress = progress))

                }
            } catch (ex: CancellationException){
                Log.d("MyLog", "Корутина Отменена")
            }

        }



    }


    fun mediaCommander(command: PlayerCommand) {
        when (command) {
            is PlayerCommand.Play -> {
                mediaInteractor.startPlayback()
                mutableMediaScreen.value = mutableMediaScreen.value!!.copy(isPlaying = true)
                startUpdateProgress()

            }
            is PlayerCommand.Pause -> {
                stopUpdateProgress()
                mediaInteractor.pausePlayback()
                mutableMediaScreen.value = mutableMediaScreen.value!!.copy(isPlaying = false)

            }

        }
    }

    fun intentGetExtraBind() {

        if (!trackFromArgs?.trackName
                .isNullOrEmpty()) {

            val trackName = trackFromArgs.trackName
            val previewUrl = trackFromArgs.previewUrl

            val trackTimeMillis = trackFromArgs.trackTimeMillis
            val artistName = trackFromArgs.artistName
            val primaryGenreName = trackFromArgs.primaryGenreName
            val country = trackFromArgs.country
            val relieseDate = trackFromArgs.releaseDate
            val artworkUrl100 = trackFromArgs.artworkUrl100

            if (trackFromArgs.collectionName
                    ?.isNullOrEmpty() == true || trackFromArgs.collectionName
                        ?.contains("No Album") == true // Если нет альбома или ответ сервера содержит No Album то убираем поле с альбомом
            ) {
                mutableMediaScreen.value = mutableMediaScreen.value!!.copy(collectionName = noAlbum)


            } else {
                val collectionName = trackFromArgs.collectionName
                    // убираем поле альбом если нет альбома
                mutableMediaScreen.value =
                    mutableMediaScreen.value!!.copy(collectionName = collectionName)
                Log.d("Mylog" , previewUrl)

                mediaInteractor.preparePlayer(previewUrl)
                mutableMediaScreen.value = mutableMediaScreen.value!!.copy(
                    trackName = trackName,
                    primaryGenreName = primaryGenreName,
                    artistName = artistName,
                    trackTimeMillis = trackTimeMillis,
                    artworkUrl100 = artworkUrl100,
                    releaseDate = relieseDate,
                    country = country
                )


            }
        } else if (!favoriteTrackInteractor.getAllTracksFromStorage().isNullOrEmpty()){
            loadLastLikedTrack()
        } else return
    }







    fun loadLastLikedTrack() {// убираем поле альбом если нет альбома
        val myTracks = favoriteTrackInteractor.getAllTracksFromStorage()
        if (!myTracks.isNullOrEmpty()) {
            val track = myTracks[0] // если myTracks не пуст, возьмем свежий трек для плеера
            val trackName = track.trackName// убираем поле альбом если нет альбома
            if (track.collectionName?.isNullOrEmpty() == true || track.collectionName?.contains("No Album") == true) {
                mutableMediaScreen.value = mutableMediaScreen.value!!.copy(collectionName = noAlbum)
            } else {
                val collectionName =
                    track.collectionName.toString() // убираем поле альбом если нет альбома
                mutableMediaScreen.value =
                    mutableMediaScreen.value!!.copy(collectionName = collectionName)
            }

            val trackTimeMillis = track.trackTimeMillis
            val artistName = track.artistName
            val primaryGenreName = track.primaryGenreName
            val country = track.country
            val relieseDate = track.releaseDate
            val artworkUrl100 = track.artworkUrl100

            val previewUrl = track.previewUrl
            mediaInteractor.preparePlayer(previewUrl)
            mutableMediaScreen.value = mutableMediaScreen.value!!.copy(
                trackName = trackName,
                trackTimeMillis = trackTimeMillis,
                artistName = artistName,
                primaryGenreName = primaryGenreName,
                country = country,
                releaseDate = relieseDate,
                artworkUrl100 = artworkUrl100
            )

        }
    }

    fun saveTrackToFavorite()= viewModelScope.launch{
        val trackToSave = trackFromArgs
        val trackEntiy = trackDbConvertor.map(trackToSave).copy(isLike = true)
        mainDb.trackDao().insertTracks(trackEntiy)


    }
    fun deleteTrackFromFavorite()= viewModelScope.launch{
        val trackToSave = trackFromArgs
        val trackEntiy = trackDbConvertor.map(trackToSave).copy(isLike = false)
        mainDb.trackDao().deleteTrackForId(trackEntiy.trackId)


    }



}



