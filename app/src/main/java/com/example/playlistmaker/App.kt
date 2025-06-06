package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.di.dataModule
import com.example.playlistmaker.di.interactorModule

import com.example.playlistmaker.di.repositoryModule
import com.example.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() { // класс АПП для смены темы

    companion object{
        lateinit var instance : App
            private set
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        val controlTheme =  Creator.provideSwitchThemeUseCase()
        controlTheme.controlThemeInOtherWindows()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)



        }


    }




}