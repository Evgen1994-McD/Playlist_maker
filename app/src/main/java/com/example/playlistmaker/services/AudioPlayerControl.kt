package com.example.playlistmaker.services

import com.example.playlistmaker.presentation.player.viewModel.PlayerState
import kotlinx.coroutines.flow.StateFlow

interface AudioPlayerControl  {
    fun getPlayerState(): StateFlow<PlayerState>
    fun startPlayback()
    fun pausePlayback()
    fun setShouldShowNotification(show: Boolean)
}