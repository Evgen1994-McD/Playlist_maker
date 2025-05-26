package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.api.SwitchThemeUseCase

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
