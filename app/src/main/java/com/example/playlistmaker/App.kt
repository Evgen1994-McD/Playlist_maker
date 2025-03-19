package com.example.playlistmaker

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.TrackStorage.Companion.PREFS_NAME
import com.example.playlistmaker.utils.Constants

class App : Application() { // класс АПП для смены темы


    override fun onCreate() {
        super.onCreate()
    }




    fun switchTheme(savedTheme: Boolean) {

        AppCompatDelegate.setDefaultNightMode(
            if (savedTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }


}
