package com.example.playlistmaker.data.dto

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.Constants

class App : Application() { // класс АПП для смены темы


    override fun onCreate() {
        super.onCreate()
    }

    fun hasBooleanValue(context: Context, key: String): Boolean {
        val sharedPref = context.getSharedPreferences(
            Constants.SHARED_PREF_THEME_NAME,
            MODE_PRIVATE
        )
        return sharedPref.contains(key)
    }


    fun isSystemInDarkTheme(context: Context): Boolean {
        val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    fun switchTheme(savedTheme: Boolean) {

        if (savedTheme!=null){
        AppCompatDelegate.setDefaultNightMode(
            if (savedTheme) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        }

    }



}