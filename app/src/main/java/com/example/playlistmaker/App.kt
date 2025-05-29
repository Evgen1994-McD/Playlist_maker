package com.example.playlistmaker

import android.app.Application
import com.example.playlistmaker.creator.Creator

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


    }




}