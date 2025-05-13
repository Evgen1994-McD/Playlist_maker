package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.MediaInteractor
import com.example.playlistmaker.domain.api.MediaPlayerManager

class MediaInteractorImpl(private var mediaPlayerManager: MediaPlayerManager,
  ) : MediaInteractor {


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
    }
