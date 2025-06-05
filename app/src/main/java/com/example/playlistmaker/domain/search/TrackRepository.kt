package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.models.Track

interface TrackRepository {
    fun searchTracks(expression: String): List<Track>
}