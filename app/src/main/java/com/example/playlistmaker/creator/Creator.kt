package com.example.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.example.playlistmaker.App
import com.example.playlistmaker.data.search.impl.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.player.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.data.search.impl.FavoriteTrackRepositoryImpl.Companion.PREFS_NAME
import com.example.playlistmaker.data.search.impl.FavoriteTrackRepositoryImpl.Companion.TRACKS_KEY
import com.example.playlistmaker.data.search.network.ITunesApi
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Creator {

    private val context by lazy { App.instance.applicationContext }

    private val iTunesBaseUrl = "https://itunes.apple.com"

    private fun createItunesApi(): ITunesApi{
        return Retrofit.Builder()
            .baseUrl(iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)

}



    private fun getTracksRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(createItunesApi()))
    }

    fun provideTracksInteractor(): TrackInteractor {
        return TracksInteractorImpl(getTracksRepository())
    }

    private fun getFavoriteSharedPrefs(): SharedPreferences{
       return  context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    }

    private fun getFavoriteTrackRepository(): FavoriteTrackRepository {
        return FavoriteTrackRepositoryImpl(getFavoriteSharedPrefs(),Gson())
    }

    fun provideFavoriteInteractor(): FavoriteTrackInteractor {
        return FavoriteTrackInteractorImpl(getFavoriteTrackRepository())
    }

    private fun getSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(context)
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