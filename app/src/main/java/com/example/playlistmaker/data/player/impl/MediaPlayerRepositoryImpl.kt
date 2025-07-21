package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import android.util.Log
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) : MediaPlayerRepository {
    companion object {


        const val STATE_IDLE = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }

    private var playerState = STATE_IDLE


    override fun stopPlayerAndReset(){
        if (mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
    }




    override fun preparePlayer(previewUrl: String) {
        if (playerState == STATE_IDLE) {
            try {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(previewUrl)
                mediaPlayer.prepareAsync()
                playerState = STATE_PREPARED
                Log.d("MyLog", "Плеер готов")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    override fun startPlayback() {
        if (playerState != STATE_PREPARED && playerState != STATE_PAUSED) return
        mediaPlayer.start()
        playerState = STATE_PLAYING
        Log.d("MyLog", "Плеер играет")

    }

    override fun pausePlayback() {
        if (playerState != STATE_PLAYING) return
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        Log.d("MyLog", "Плеер на Паузе")
    }

    override fun releasePlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
        playerState = STATE_IDLE
        Log.d("MyLog", "Плеер освобождён")
    }

    override fun addListeners(onPreparedListener: () -> Unit, onCompletionListener: () -> Unit) {
        mediaPlayer.setOnPreparedListener(MediaPlayer.OnPreparedListener {
            playerState = STATE_PREPARED
            Log.d("MyLog", "Плеер точно 100% готов")

            onPreparedListener()
        })
        mediaPlayer.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            playerState = STATE_IDLE
            Log.d("MyLog", "Плеер точно закончил играть, статус $playerState")
            onCompletionListener()
        })
    }

    override fun updateProgress(): String {
        if (playerState == STATE_PLAYING || playerState== STATE_PAUSED) {
            val formattedTime =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            return formattedTime
        }else return "00:00"
    }



}