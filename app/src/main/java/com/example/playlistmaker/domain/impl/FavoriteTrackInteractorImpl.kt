package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.FavoriteTrackInteractor
import com.example.playlistmaker.domain.api.FavoriteTrackRepository
import com.example.playlistmaker.domain.models.Track

class FavoriteTrackInteractorImpl(private val repository: FavoriteTrackRepository) :
    FavoriteTrackInteractor {


    override fun addTrack(track: Track) {
        repository.addTrack(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun getAllTracksFromStorage(): List<Track> {
        return repository.getAllTracks()
    }



}