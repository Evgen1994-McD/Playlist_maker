package com.example.playlistmaker.domain.player

interface MediaPlayerRepository {

    fun preparePlayer(previewUrl: String)
    fun startPlayback()
    fun pausePlayback()
    fun releasePlayer()
    fun addListeners(onPreparedListener: () -> Unit, onCompletionListener: () -> Unit)
    fun updateProgress(): String
    fun stopPlayerAndReset()
}