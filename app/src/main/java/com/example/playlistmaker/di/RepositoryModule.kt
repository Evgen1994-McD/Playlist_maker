package com.example.playlistmaker.di

import com.example.playlistmaker.data.search.impl.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.domain.search.FavoriteTrackRepository
import com.example.playlistmaker.domain.search.TrackRepository
import org.koin.dsl.module



    val repositoryModule = module {

        single<FavoriteTrackRepository>{
            FavoriteTrackRepositoryImpl(get(), get())
        }

        single<TrackRepository>{
            TrackRepositoryImpl(get())
        }


    }


