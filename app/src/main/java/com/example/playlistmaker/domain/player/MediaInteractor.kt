package com.example.playlistmaker.domain.player

interface MediaInteractor {

    fun preparePlayer(previewUrl: String)
    fun startPlayback()
    fun pausePlayback()
    fun releasePlayer()
    fun addListeners(onPreparedListener: () -> Unit, onCompletionListener: () -> Unit)
    fun updateProgress(): String
}