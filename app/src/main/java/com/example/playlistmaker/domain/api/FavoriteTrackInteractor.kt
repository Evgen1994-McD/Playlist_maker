package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface FavoriteTrackInteractor {

    fun clearHistory()
    fun addTrack(track: Track)
    fun getAllTracksFromStorage(): List<Track>


}