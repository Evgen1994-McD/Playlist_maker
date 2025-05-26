package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.MediaInteractor
import com.example.playlistmaker.domain.api.MediaPlayerRepository

class MediaInteractorImpl(
    private var mediaPlayerRepository: MediaPlayerRepository,
) : MediaInteractor {
    private val debounceIntervalMillis = 1000L // Интервал блокировки в миллисекундах
    private var lastClickTime = System.currentTimeMillis() // Хранение последнего времени клика

    override fun preparePlayer(previewUrl: String) {
        mediaPlayerRepository.preparePlayer(previewUrl)
    }

    override fun startPlayback() {

        mediaPlayerRepository.startPlayback()

    }

    override fun pausePlayback() {

        mediaPlayerRepository.pausePlayback()

    }

    override fun releasePlayer() {
        mediaPlayerRepository.releasePlayer()
    }

    override fun addListeners(onPreparedListener: () -> Unit, onCompletionListener: () -> Unit) {
        mediaPlayerRepository.addListeners(onPreparedListener, onCompletionListener)
    }

    override fun updateProgress(): String {
        val progress = mediaPlayerRepository.updateProgress()
        return progress
    }



}
