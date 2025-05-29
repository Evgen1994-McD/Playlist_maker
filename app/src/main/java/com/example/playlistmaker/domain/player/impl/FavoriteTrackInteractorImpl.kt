package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.FavoriteTrackInteractor
import com.example.playlistmaker.domain.player.FavoriteTrackRepository
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