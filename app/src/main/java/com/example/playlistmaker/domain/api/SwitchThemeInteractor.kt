package com.example.playlistmaker.domain.api

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.App
import com.google.android.material.switchmaterial.SwitchMaterial

interface SwitchThemeInteractor {
    fun switchThemeModeBySettings(
        switch: SwitchMaterial?,
        applicationContext: Context,
        context: Context,
        sharedPrefs: SharedPreferences,
        activity: Activity
    )
    fun controlThemeInOtherWindows(applicationContext: App,
                                   context: Context,
                                   sharedPrefs: SharedPreferences,
                                   activity: Activity)

}