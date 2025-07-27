package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.TrackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(
    private val repository: TrackRepository

) : TrackInteractor {

    companion object{
        private const val exceptionStateString = "Exception"
    }

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {


        return repository.searchTracks(expression).map { results ->
            if (results != null) {
                Pair(results, null)
            } else {
                Pair(null, exceptionStateString)
            }
        }
    }
}












