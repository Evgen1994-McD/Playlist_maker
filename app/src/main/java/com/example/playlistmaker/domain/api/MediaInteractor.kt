package com.example.playlistmaker.domain.api

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.domain.models.Track

interface MediaInteractor {

    fun preparePlayer(previewUrl: String)
    fun startPlayback()
    fun pausePlayback()
    fun releasePlayer()
    fun addListeners(onPreparedListener: () -> Unit, onCompletionListener: () -> Unit)
    fun updateProgress(): String
    //fun clickDebounce(): Boolean метод убираю, потом решу нужен или нет
}