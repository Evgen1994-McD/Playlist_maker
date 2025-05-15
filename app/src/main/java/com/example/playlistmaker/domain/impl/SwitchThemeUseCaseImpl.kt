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
        switch: SwitchMaterial?,
        applicationContext: Context,
    ) {
        val theme = repository.controlAppThemeMode(applicationContext)

        switch!!.isChecked = theme
        switch.setOnCheckedChangeListener { _, isChecked ->
            // Сохранить новое значение темы in Sharedpreferences
            repository.saveCurrentThemeToShared(isChecked)
                repository.switchTheme(isChecked)

        }

    }


    override fun controlThemeInOtherWindows(
        applicationContext: App
    ) {
        val theme = repository.controlAppThemeMode(applicationContext)
     repository.switchTheme(theme)

    }
}
