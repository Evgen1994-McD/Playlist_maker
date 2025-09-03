package com.example.playlistmaker.ui.media

import com.example.playlistmaker.domain.models.Track

interface OnItemLongClickListener {
    fun onItemLongClick(track: Track)
}