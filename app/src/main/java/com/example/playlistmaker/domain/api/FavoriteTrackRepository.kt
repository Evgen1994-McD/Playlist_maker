package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.models.Track

interface FavoriteTrackRepository {
    fun addTrack(track: Track){}

    fun getAllTracks(): List<Track>

    fun loadTracksFromPrefs(): List<TrackDto>

    fun saveTracksToPrefs()

    fun clearHistory()

    fun createTrackDtoFromTrack(track: Track) : TrackDto


    fun createTrackFromTrackDto(trackDto: TrackDto) : Track
}