package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.MediaInteractor
import com.example.playlistmaker.domain.player.MediaPlayerRepository

class MediaInteractorImpl(
    private var mediaPlayerRepository: MediaPlayerRepository,
) : MediaInteractor {


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
