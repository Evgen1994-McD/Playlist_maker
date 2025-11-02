package com.example.playlistmaker.presentation.media

import com.example.playlistmaker.domain.models.PlayList

interface onPlaylistClickListener {
    fun onPlaylistClicked(playList: PlayList)
}