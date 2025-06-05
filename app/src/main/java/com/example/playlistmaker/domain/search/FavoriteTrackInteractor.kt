package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.models.Track

interface FavoriteTrackInteractor {

    fun clearHistory()
    fun addTrack(track: Track)
    fun getAllTracksFromStorage(): List<Track>


}