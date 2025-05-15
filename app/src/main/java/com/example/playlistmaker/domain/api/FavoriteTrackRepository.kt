package com.example.playlistmaker.domain.api


import com.example.playlistmaker.domain.models.Track

interface FavoriteTrackRepository {
    fun addTrack(track: Track) {}

    fun getAllTracks(): List<Track>



    fun clearHistory()



}