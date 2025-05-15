package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.Constants

class App : Application() { // класс АПП для смены темы

    companion object{
        lateinit var instance : App
            private set
    }


    override fun onCreate() {
        super.onCreate()
        instance = this
        val controlTheme =  Creator.provideSwitchThemeUseCase()
        controlTheme.controlThemeInOtherWindows(instance)


    }




}