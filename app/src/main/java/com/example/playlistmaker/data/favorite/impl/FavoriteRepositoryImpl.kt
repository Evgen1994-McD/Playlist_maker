package com.example.playlistmaker.data.favorite.impl

import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.db.MainDb
import com.example.playlistmaker.data.db.converters.TrackDbConvertor
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.db.FavoriteRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class FavoriteRepositoryImpl(
    private val mainDb: MainDb,
    private val trackDbConvertor: TrackDbConvertor
) : FavoriteRepository {

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
     val tracks = (mainDb.trackDao().getTracks()).reversed()
        emit(convertTracksFromTrackEntity(tracks))
    }


    private fun convertTracksFromTrackEntity(tracks: List<TrackEntity>): List<Track>{
        return tracks.map {
            trackEntity ->
            trackDbConvertor.map(trackEntity)
        }
    }

    private fun convertTrackEntityFromTrack(track: Track): TrackEntity{
        return trackDbConvertor.map(track)
    }



   override suspend fun saveTrackToFavorite(track:Track){
       val trackToSave = convertTrackEntityFromTrack(track)
        val trackEntiy = trackToSave.copy(isLike = true)
        mainDb.trackDao().insertTracks(trackEntiy.copy(isLike = true))


    }

    override suspend fun deleteTrackFromFavorite(track: Track){
        val trackToDelete = convertTrackEntityFromTrack(track)
        val trackEntiy = trackToDelete.copy(isLike = false)
        mainDb.trackDao().deleteTrackForId(trackEntiy.trackId)


    }


    override suspend fun controlIsLike(track: Track) : Boolean {
        val foundTracks = mainDb.trackDao().selectTrackForId(track.trackId)
        if (foundTracks.isNotEmpty()) {
          return true
        } else {
      return false
        }
    }



}