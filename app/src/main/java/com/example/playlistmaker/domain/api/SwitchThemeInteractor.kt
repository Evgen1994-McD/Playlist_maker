package com.example.playlistmaker.domain.api

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.android.material.switchmaterial.SwitchMaterial

interface SwitchThemeInteractor {
    fun switchThemeModeBySettings(
        switch: SwitchMaterial?,
        applicationContext: Context,
        context: Context,
        sharedPrefs: SharedPreferences,
        activity: Activity
    )
}