package com.example.playlistmaker.domain.settings

interface SwitchThemeUseCase {
    fun switchThemeModeBySettings(
        myTheme : Boolean
    )

    fun controlThemeInOtherWindows(
    ): Boolean


}