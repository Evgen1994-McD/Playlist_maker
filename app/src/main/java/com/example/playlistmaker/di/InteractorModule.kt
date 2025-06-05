package com.example.playlistmaker.di

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.data.search.impl.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.domain.search.FavoriteTrackInteractor
import com.example.playlistmaker.domain.search.FavoriteTrackRepository
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.search.impl.FavoriteTrackInteractorImpl
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import org.koin.dsl.module
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors




    val interactorModule = module {

        single {  // хендлер для интерактора
            Handler(
                Looper.getMainLooper()
            )
        }

        single { Executors.newSingleThreadExecutor() } // Экзекутор для интерактора

        single<TrackInteractor>{
            TracksInteractorImpl(get(), get(), get())
        }


        single<FavoriteTrackInteractor>{
            FavoriteTrackInteractorImpl(get())
        }


    }

