package com.example.playlistmaker.di

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.data.search.impl.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.data.search.impl.TrackRepositoryImpl
import com.example.playlistmaker.domain.db.FavoriteInteractor
import com.example.playlistmaker.domain.db.impl.FavoriteInteractorImpl
import com.example.playlistmaker.domain.player.MediaInteractor
import com.example.playlistmaker.domain.player.impl.MediaInteractorImpl
import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import com.example.playlistmaker.domain.playlists.impl.PlaylistInteractorImpl
import com.example.playlistmaker.domain.search.FavoriteTrackInteractor
import com.example.playlistmaker.domain.search.FavoriteTrackRepository
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.search.impl.FavoriteTrackInteractorImpl
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.settings.OpenUrlUseCase
import com.example.playlistmaker.domain.settings.SendSuppEmailUseCase
import com.example.playlistmaker.domain.settings.ShareAppUseCase
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase
import com.example.playlistmaker.domain.settings.impl.OpenUrlUseCaseImpl
import com.example.playlistmaker.domain.settings.impl.SendSuppEmailUseCaseImpl
import com.example.playlistmaker.domain.settings.impl.ShareAppUseCaseImpl
import com.example.playlistmaker.domain.settings.impl.SwitchThemeUseCaseImpl
import org.koin.dsl.module
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val interactorModule = module {

    factory {  // хендлер для интерактора
        Handler(
            Looper.getMainLooper()
        )
    }
    single { Executors.newSingleThreadExecutor() } // Экзекутор для интерактора

    factory<TrackInteractor> {
        TracksInteractorImpl(get())
    }
    single<FavoriteTrackInteractor> {
        FavoriteTrackInteractorImpl(get())
    }
//Settings
    factory<SwitchThemeUseCase> {
        SwitchThemeUseCaseImpl(get())
    }
    factory<OpenUrlUseCase> {
        OpenUrlUseCaseImpl(get())
    }

    factory<SendSuppEmailUseCase> {
        SendSuppEmailUseCaseImpl(get())
    }
    factory<ShareAppUseCase> {
        ShareAppUseCaseImpl(get())
    }

    //Media
    factory<MediaInteractor> {
        MediaInteractorImpl(get())
    }

single<FavoriteInteractor> {
    FavoriteInteractorImpl(get())
}

    single<PlaylistInteractor> {
    PlaylistInteractorImpl(get())
}


}

