package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class MediaPlayerRepositoryImpl(val mediaPlayer: MediaPlayer) : MediaPlayerRepository {
    companion object {


        const val STATE_IDLE = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }

    private var playerState = STATE_IDLE

    override fun preparePlayer(previewUrl: String) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(previewUrl)
            mediaPlayer.prepareAsync()
            playerState = STATE_PREPARED
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun startPlayback() {
        if (playerState != STATE_PREPARED && playerState != STATE_PAUSED) return
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayback() {
        if (playerState != STATE_PLAYING) return
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer.release()
        playerState = STATE_IDLE
    }

    override fun addListeners(onPreparedListener: () -> Unit, onCompletionListener: () -> Unit) {
        mediaPlayer.setOnPreparedListener(MediaPlayer.OnPreparedListener {
            onPreparedListener()
        })
        mediaPlayer.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            onCompletionListener()
        })
    }

    override fun updateProgress(): String {

        val formattedTime =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        return formattedTime
    }


}