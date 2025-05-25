package com.example.playlistmaker.presentation.models

sealed class MediaPlayerCommand {
    object Play : MediaPlayerCommand()
    object Pause: MediaPlayerCommand()
    data class SeekTo(val position : Int) : MediaPlayerCommand()

}