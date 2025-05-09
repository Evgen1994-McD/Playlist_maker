package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface FavoriteTrackInteractor {

    fun getAllTracks(consumer: FavoriteTrackConsumer)

    interface FavoriteTrackConsumer{
        fun consume(myTracks : List<Track>)
    }
}