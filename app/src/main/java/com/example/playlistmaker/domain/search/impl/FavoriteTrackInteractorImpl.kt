package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.FavoriteTrackRepository
import com.example.playlistmaker.domain.search.FavoriteTrackInteractor

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