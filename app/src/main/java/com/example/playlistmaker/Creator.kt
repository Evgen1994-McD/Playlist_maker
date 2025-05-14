package com.example.playlistmaker

import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.repositories.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.data.repositories.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.repositories.MediaPlayerManagerImpl
import com.example.playlistmaker.data.repositories.SettingsReposytoryImpl
import com.example.playlistmaker.domain.api.FavoriteTrackInteractor
import com.example.playlistmaker.domain.api.FavoriteTrackRepository
import com.example.playlistmaker.domain.api.MediaInteractor
import com.example.playlistmaker.domain.api.MediaPlayerRepository
import com.example.playlistmaker.domain.api.OpenUrlUseCase
import com.example.playlistmaker.domain.api.SendSuppEmailUseCase
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.api.ShareAppUseCase
import com.example.playlistmaker.domain.api.SwitchThemeUseCase
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.impl.FavoriteTrackInteractorImpl
import com.example.playlistmaker.domain.impl.MediaInteractorImpl
import com.example.playlistmaker.domain.impl.OpenUrlUseCaseImpl
import com.example.playlistmaker.domain.impl.SendSuppEmailUseCaseImpl
import com.example.playlistmaker.domain.impl.ShareAppUseCaseImpl
import com.example.playlistmaker.domain.impl.SwitchThemeUseCaseImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {

    private val context by lazy { App.instance.applicationContext }


    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TrackInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getFavoriteTrackRepository(context: Context): FavoriteTrackRepository {
        return FavoriteTrackRepositoryImpl(context)
    }

    fun provideFavoriteInteractor(context: Context): FavoriteTrackInteractor {
        return FavoriteTrackInteractorImpl(getFavoriteTrackRepository(context))
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsReposytoryImpl(context)
    }


    fun provideShareAppUseCase(): ShareAppUseCase {
        return ShareAppUseCaseImpl(getSettingsRepository())
    }

    fun provideSendSuppEmailUseCase(): SendSuppEmailUseCase {
        return SendSuppEmailUseCaseImpl(getSettingsRepository())
    }

    fun provideOpenUrlUseCase(): OpenUrlUseCase {
        return OpenUrlUseCaseImpl(getSettingsRepository())
    }

    fun provideSwitchThemeUseCase(): SwitchThemeUseCase {
        return SwitchThemeUseCaseImpl(getSettingsRepository())
    }


    fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }


    fun getMediaPlayerManager(): MediaPlayerRepository {
        return MediaPlayerManagerImpl(getMediaPlayer())
    }

    fun provideMediaInteractor(): MediaInteractor {
        return MediaInteractorImpl(getMediaPlayerManager())
    }

}