package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.FavoriteInteractor
import com.example.playlistmaker.domain.db.FavoriteRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(
    private val favoriteRepository: FavoriteRepository
) : FavoriteInteractor {
    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteRepository.getFavoriteTracks()
    }

    override suspend fun saveTrackToFavorite(track:Track){
        favoriteRepository.saveTrackToFavorite(track)
    }


    override suspend fun deleteTrackFromFavorite(track: Track){
        favoriteRepository.deleteTrackFromFavorite(track)
    }

    override suspend fun controlIsLike(track: Track) : Boolean{
       return favoriteRepository.controlIsLike(track)
    }


}