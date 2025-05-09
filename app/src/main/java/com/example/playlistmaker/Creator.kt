package com.example.playlistmaker

import android.app.Activity
import android.content.Context
import com.example.playlistmaker.data.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.api.FavoriteTrackInteractor
import com.example.playlistmaker.domain.api.FavoriteTrackRepository
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.impl.FavoriteTrackInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl
import com.example.playlistmaker.ui.activity.SearchActivity

object Creator {
    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TrackInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getFavoriteTrackRepository(context : Context) : FavoriteTrackRepository {
        return FavoriteTrackRepositoryImpl(context)
    }

    fun provideFaworiteInteractor(context: Context) : FavoriteTrackInteractor {
        return FavoriteTrackInteractorImpl(getFavoriteTrackRepository(context))
    }


}