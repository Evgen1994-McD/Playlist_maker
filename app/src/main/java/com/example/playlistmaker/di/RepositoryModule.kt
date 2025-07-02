package com.example.playlistmaker.di

import com.example.playlistmaker.data.player.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.search.impl.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.search.FavoriteTrackRepository
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.settings.SettingsRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module



    val repositoryModule = module {

        factory<FavoriteTrackRepository>{
            FavoriteTrackRepositoryImpl(get(), get())
        }

        factory<TrackRepository>{
            TrackRepositoryImpl(get())
        }

        factory<SettingsRepository>{
            SettingsRepositoryImpl(get())
        }

        factory<MediaPlayerRepository>{
            MediaPlayerRepositoryImpl(get(named(PLAYER_SCOPE_NAME)))
        }


    }


