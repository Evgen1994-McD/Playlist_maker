package com.example.playlistmaker.creator

import android.media.MediaPlayer
import com.example.playlistmaker.App
import com.example.playlistmaker.data.search.impl.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.player.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.settings.impl.SettingsReposytoryImpl
import com.example.playlistmaker.domain.search.FavoriteTrackInteractor
import com.example.playlistmaker.domain.search.FavoriteTrackRepository
import com.example.playlistmaker.domain.player.MediaInteractor
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.settings.OpenUrlUseCase
import com.example.playlistmaker.domain.settings.SendSuppEmailUseCase
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.ShareAppUseCase
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.search.impl.FavoriteTrackInteractorImpl
import com.example.playlistmaker.domain.player.impl.MediaInteractorImpl
import com.example.playlistmaker.domain.settings.impl.OpenUrlUseCaseImpl
import com.example.playlistmaker.domain.settings.impl.SendSuppEmailUseCaseImpl
import com.example.playlistmaker.domain.settings.impl.ShareAppUseCaseImpl
import com.example.playlistmaker.domain.settings.impl.SwitchThemeUseCaseImpl
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl

object Creator {

    private val context by lazy { App.instance.applicationContext }


    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTracksInteractor(): TrackInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getFavoriteTrackRepository(): FavoriteTrackRepository {
        return FavoriteTrackRepositoryImpl(context)
    }

    fun provideFavoriteInteractor(): FavoriteTrackInteractor {
        return FavoriteTrackInteractorImpl(getFavoriteTrackRepository())
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


    fun getMediaPlayerInteractor(): MediaPlayerRepository {
        return MediaPlayerRepositoryImpl(getMediaPlayer())
    }

    fun provideMediaInteractor(): MediaInteractor {
        return MediaInteractorImpl(getMediaPlayerInteractor())
    }

}