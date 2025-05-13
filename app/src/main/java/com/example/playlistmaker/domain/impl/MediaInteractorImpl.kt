package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.MediaInteractor
import com.example.playlistmaker.domain.api.MediaPlayerManager

class MediaInteractorImpl(
    private var mediaPlayerManager: MediaPlayerManager,
) : MediaInteractor {
    private val debounceIntervalMillis = 1000L // Интервал блокировки в миллисекундах
    private var lastClickTime = System.currentTimeMillis() // Хранение последнего времени клика

    override fun preparePlayer(previewUrl: String) {
        mediaPlayerManager.preparePlayer(previewUrl)
    }

    override fun startPlayback() {

        mediaPlayerManager.startPlayback()

    }

    override fun pausePlayback() {

        mediaPlayerManager.pausePlayback()

    }

    override fun releasePlayer() {
        mediaPlayerManager.releasePlayer()
    }

    override fun addListeners(onPreparedListener: () -> Unit, onCompletionListener: () -> Unit) {
        mediaPlayerManager.addListeners(onPreparedListener, onCompletionListener)
    }

    override fun updateProgress(): String {
        val progress = mediaPlayerManager.updateProgress()
        return progress
    }


    override fun clickDebounce(): Boolean {

        val now = System.currentTimeMillis()

        // Проверяем прошло ли достаточно времени с момента последнего клика
        if (now - lastClickTime >= debounceIntervalMillis) {
            lastClickTime = now // Обновляем время последнего клика
            return true // Клик разрешен
        }
        return false // Клик запрещен
    }

}
