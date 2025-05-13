package com.example.playlistmaker.domain.api

interface MediaPlayerManager {

    fun preparePlayer(previewUrl: String)
    fun startPlayback()
    fun pausePlayback()
    fun releasePlayer()
    fun addListeners(onPreparedListener: () -> Unit, onCompletionListener: () -> Unit)
    fun updateProgress(): String
}