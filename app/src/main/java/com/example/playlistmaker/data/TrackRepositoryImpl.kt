package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(expression: String): List<Track> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        if (response.resultCode == 200) {
            return (response as TrackResponse).results.map {
                Track(
                    it.trackId,
                    it.artistName,
                    it.trackTimeMillis,
                    it.artworkUrl100,
                    it.trackName,
                    it.previewUrl,
                    it.collectionName,
                    it.releaseDate,
                    it.country,
                    it.primaryGenreName
                )
            }
        } else {
            return emptyList()
        }
    }
}
