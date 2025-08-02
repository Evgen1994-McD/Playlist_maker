package com.example.playlistmaker.data.favorite.impl

import com.example.playlistmaker.data.db.MainDb
import com.example.playlistmaker.data.db.converters.TrackDbConvertor
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.db.FavoriteRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val mainDb: MainDb,
    private val trackDbConvertor: TrackDbConvertor
) : FavoriteRepository {

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
     val tracks = mainDb.trackDao().getTracks()
        emit(convertTracksFromTrackEntity(tracks))
    }


    private fun convertTracksFromTrackEntity(tracks: List<TrackEntity>): List<Track>{
        return tracks.map {
            trackEntity ->
            trackDbConvertor.map(trackEntity)
        }
    }
}