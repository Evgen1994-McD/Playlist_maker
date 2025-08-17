package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.TrackInteractor
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
    fun getFavoriteTracks(): Flow<List<Track>>
    suspend fun saveTrackToFavorite(track: Track)
    suspend fun deleteTrackFromFavorite(track: Track)
    suspend fun controlIsLike(track: Track): Boolean
}