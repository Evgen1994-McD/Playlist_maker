package com.example.playlistmaker.di

import com.example.playlistmaker.data.player.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.search.impl.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.search.FavoriteTrackRepository
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.settings.SettingsRepository
import org.koin.dsl.module



    val repositoryModule = module {

        single<FavoriteTrackRepository>{
            FavoriteTrackRepositoryImpl(get(), get())
        }

        single<TrackRepository>{
            TrackRepositoryImpl(get())
        }

        single<SettingsRepository>{
            SettingsRepositoryImpl(get())
        }

        single<MediaPlayerRepository>{
            MediaPlayerRepositoryImpl(get())
        }


    }


