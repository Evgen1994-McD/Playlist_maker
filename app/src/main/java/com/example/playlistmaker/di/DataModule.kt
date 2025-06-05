package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.data.search.impl.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.search.network.ITunesApi
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.search.FavoriteTrackRepository
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.utils.Constants
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory




    val dataModule = module {

        single<ITunesApi>{ // это вызов itines aPi
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


        factory { Gson() }  // это Gson()

            /* Описали все состявляющие репозиториев ( фаворит  трак репозиторий и Трек репозиторий,
            ниже мы их получение тоже опишем
             */



//        single<FavoriteTrackRepository>{
//            FavoriteTrackRepositoryImpl(get(), get())
//        }
//
//        single<TrackRepository> {
//            TrackRepositoryImpl(get())
//        }

        single<NetworkClient> {
            RetrofitNetworkClient(get())
        }
/* выше мы описали все зависимости для слоя данных ?
Трек репозитори и Фаворит трек репозитори не отсюда?
 */

    }
