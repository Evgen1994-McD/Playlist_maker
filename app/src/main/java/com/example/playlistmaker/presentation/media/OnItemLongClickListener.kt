package com.example.playlistmaker.presentation.media

import com.example.playlistmaker.domain.models.Track

interface OnItemLongClickListener {
    fun onItemLongClick(track: Track)
}