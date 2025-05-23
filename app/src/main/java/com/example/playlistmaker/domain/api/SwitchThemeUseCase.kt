package com.example.playlistmaker.domain.api

import android.app.Activity
import android.content.Context
import com.example.playlistmaker.App
import com.google.android.material.switchmaterial.SwitchMaterial

interface SwitchThemeUseCase {
    fun switchThemeModeBySettings(
        myTheme : Boolean
    )

    fun controlThemeInOtherWindows(
    ): Boolean


}