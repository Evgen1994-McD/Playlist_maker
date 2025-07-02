package com.example.playlistmaker.di
import org.koin.core.scope.Scope
import org.koin.dsl.module
import org.koin.dsl.scoped
import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import com.example.playlistmaker.data.search.impl.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.data.search.network.ITunesApi
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.search.FavoriteTrackRepository
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.ui.player.fragments.PlayerFragment
import com.example.playlistmaker.utils.Constants
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.getScopeName
import org.koin.core.module._scopedInstanceFactory
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

        single { Gson() }  // это Gson()

//        factory<MediaPlayer>{  //инициализировал медиаплеер
//            MediaPlayer()
//        }

        scope<PlayerFragment> {
            scoped { MediaPlayer()}
        }

        single<Application> {androidContext() as Application  }





        single<NetworkClient> {
            RetrofitNetworkClient(get())
        }



    }

