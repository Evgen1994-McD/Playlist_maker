package com.example.playlistmaker.domain.api

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.domain.models.Track

interface MediaInteractor {

    fun preparePlayer()

    fun startPlayer()

    fun pausePlayer()

    fun playBackControl()

    fun intentGetExtraBind(intent : Intent, myTracks : List<Track>)

    fun loadLastLikedTrack(track: Track)

    fun stopUpdateProgress()

    fun startUpdateProgress()

    fun updateProgress()

    fun showAudioPlayerScreen(intent: Intent, context: Context)  // Восстановим активность

}