package com.example.playlistmaker.domain.impl

import android.content.Context
import com.example.playlistmaker.domain.api.ControlAppThemeInteractor
import com.example.playlistmaker.domain.api.SettingsRepository

class ControlAppThemeInteractorImpl(private val repository: SettingsRepository) : ControlAppThemeInteractor{
    override fun controlAppThemeMode(applicationContext: Context, darkTheme: Boolean) {

    }

}