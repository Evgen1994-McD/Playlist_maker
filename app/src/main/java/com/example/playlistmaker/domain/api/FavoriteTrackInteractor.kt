package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface FavoriteTrackInteractor {

    fun getAllTracks(consumer: FavoriteTrackConsumer)
    fun clearHistory()
    fun addTrack(track: Track)
    fun getter(): List<Track>
    interface FavoriteTrackConsumer{
        fun consume(myTracks : List<Track>)
        fun onFailure(error: Throwable)

    }
}