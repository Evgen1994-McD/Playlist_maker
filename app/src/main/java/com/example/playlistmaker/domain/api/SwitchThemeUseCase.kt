package com.example.playlistmaker.domain.api

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.App
import com.google.android.material.switchmaterial.SwitchMaterial

interface SwitchThemeUseCase {
    fun switchThemeModeBySettings(
        switch: SwitchMaterial?,
        applicationContext: Context,
        context: Context,
        activity: Activity
    )
    fun controlThemeInOtherWindows(applicationContext: App,
                                   context: Context,
                                   activity: Activity)

}