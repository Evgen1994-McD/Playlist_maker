package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase

class SwitchThemeUseCaseImpl(private val repository: SettingsRepository) :
    SwitchThemeUseCase {
    override fun switchThemeModeBySettings(
        myTheme : Boolean

    ) {

            repository.saveCurrentThemeToShared(myTheme)
                repository.switchTheme(myTheme)

        }




    override fun controlThemeInOtherWindows(
    ) : Boolean {
        val theme = repository.controlAppThemeMode()
     repository.switchTheme(theme)
return theme
    }

}
