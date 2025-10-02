package com.example.playlistmaker.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.playlistmaker.ui.player.viewModel.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

internal class MusicService : Service() {

    private var timerJob: Job? = null

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var previewUrl: String

    private val binder = MusicServiceBinder()

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
        previewUrl = intent?.getStringExtra("track") ?: ""
        preparePlayer(previewUrl)
        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)


    }


    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default())
    val playerState = _playerState.asStateFlow()



    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()

    }



    fun stopPlayerAndReset() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
    }


    fun preparePlayer(previewUrl: String) {
        if (previewUrl.isEmpty()) return
        if (playerState == PlayerState.Default()) {
            try {
                mediaPlayer?.reset()
                mediaPlayer?.setDataSource(previewUrl)
                mediaPlayer?.prepareAsync()
                mediaPlayer?.setOnPreparedListener {
                    Log.d("My_Log", "Media Player prepared")
                    _playerState.value = PlayerState.Prepared()

                }
                mediaPlayer?.setOnCompletionListener {
                    Log.d("MyLog", "Playback completed")
                    _playerState.value  = PlayerState.Prepared()

                }
                _playerState.value  = PlayerState.Prepared()
                Log.d("MyLog", "Плеер готов")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun startPlayback() {
        if (playerState != PlayerState.Prepared() && playerState != PlayerState.Paused(updateProgress())) return
        mediaPlayer.start()
        _playerState.value = PlayerState.Playing(updateProgress())
startTimer()
        Log.d("MyLog", "Плеер играет")

    }

    fun pausePlayback() {
        if (playerState != PlayerState.Playing(updateProgress())) return
        mediaPlayer.pause()
        _playerState.value  = PlayerState.Paused(updateProgress())
        timerJob?.cancel()
        Log.d("MyLog", "Плеер на Паузе")
    }

    fun releasePlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        timerJob?.cancel()
        mediaPlayer.release()
        _playerState.value  = PlayerState.Default()
        Log.d("MyLog", "Плеер освобождён")
    }


    fun updateProgress(): String {
        if(playerState == PlayerState.Playing(updateProgress()) || playerState == PlayerState.Paused(updateProgress()))
        {
            val formattedTime =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            return formattedTime
        } else return "00:00"
    }

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer?.isPlaying == true) {
                delay(300L)
                _playerState.value  = PlayerState.Playing(updateProgress())

            }
        }

    }


}