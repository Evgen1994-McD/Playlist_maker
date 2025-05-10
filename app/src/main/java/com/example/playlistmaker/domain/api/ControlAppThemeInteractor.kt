package com.example.playlistmaker.domain.api

import android.content.Context

interface ControlAppThemeInteractor {
    fun controlAppThemeMode(applicationContext: Context, darkTheme : Boolean)
}