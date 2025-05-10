package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.repositories.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.data.repositories.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repositories.SettingsReposytoryImpl
import com.example.playlistmaker.domain.api.FavoriteTrackInteractor
import com.example.playlistmaker.domain.api.FavoriteTrackRepository
import com.example.playlistmaker.domain.api.OpenUrlInDefaultBrowserInteractor
import com.example.playlistmaker.domain.api.SendSuppEmailInteractor
import com.example.playlistmaker.domain.api.SettingsInteractor
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.api.ShareAppInteractor
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.impl.FavoriteTrackInteractorImpl
import com.example.playlistmaker.domain.impl.OpenUrlInDefaultBrowserImpl
import com.example.playlistmaker.domain.impl.SendSuppEmailInteractorImpl
import com.example.playlistmaker.domain.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.impl.ShareAppInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

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

    private fun getSettingsRepository(): SettingsRepository{
        return SettingsReposytoryImpl()
    }

    fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository())
    }

    fun provideShareAppInteractor() : ShareAppInteractor {
        return ShareAppInteractorImpl(getSettingsRepository())
    }

    fun provideSendSuppEmailInteracto(): SendSuppEmailInteractor {
        return SendSuppEmailInteractorImpl(getSettingsRepository())
    }

    fun provideOpenUrlInteractor() : OpenUrlInDefaultBrowserInteractor {
        return OpenUrlInDefaultBrowserImpl(getSettingsRepository())
    }

}