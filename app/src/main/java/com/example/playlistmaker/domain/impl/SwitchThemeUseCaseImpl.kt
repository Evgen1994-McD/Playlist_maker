package com.example.playlistmaker.domain.impl

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.api.SwitchThemeUseCase
import com.google.android.material.switchmaterial.SwitchMaterial

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
