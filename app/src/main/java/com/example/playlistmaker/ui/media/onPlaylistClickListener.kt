package com.example.playlistmaker.ui.media

import com.example.playlistmaker.domain.models.PlayList

interface onPlaylistClickListener {
    fun onPlaylistClicked(playList: PlayList)
}