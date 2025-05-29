package com.example.playlistmaker.domain.player

interface MediaInteractor {

    fun preparePlayer(previewUrl: String)
    fun startPlayback()
    fun pausePlayback()
    fun releasePlayer()
    fun addListeners(onPreparedListener: () -> Unit, onCompletionListener: () -> Unit)
    fun updateProgress(): String
    //fun clickDebounce(): Boolean метод убираю, потом решу нужен или нет
}