package com.example.playlistmaker.di

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import com.example.playlistmaker.data.search.network.ITunesApi
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.utils.Constants
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {
    single<ITunesApi> { // это вызов itines aPi
        val iTunesBaseUrl = "https://itunes.apple.com"
        Retrofit.Builder()
            .baseUrl(iTunesBaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }
    single {  // это вызов шаред префс
        androidContext()
            .getSharedPreferences(Constants.TRACK_STORAGE_PREFS_NAME, Context.MODE_PRIVATE)
    }

    single { Gson() }  // это Gson()
//
    factory<MediaPlayer> {  //инициализировал медиаплеер
        MediaPlayer()
    }


    single<Application> { androidContext() as Application }





    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }


// Другие зависимости


}
