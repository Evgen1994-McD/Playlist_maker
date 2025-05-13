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
        context: Context,
        activity: Activity

    ) {
        val theme = repository.controlAppThemeMode(applicationContext, context)

        switch!!.isChecked = theme
        switch.setOnCheckedChangeListener { _, isChecked ->
            // Сохранить новое значение темы in Sharedpreferences
            repository.saveCurrentThemeToShared(context, isChecked)

            // Переключить тему
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            // Перезапустить активность для применения новой темы
            recreate(activity)
        }

    }


    override fun controlThemeInOtherWindows(

        applicationContext: App,
        context: Context,
        activity: Activity
    ) {
        val theme = repository.controlAppThemeMode(applicationContext, context)
        applicationContext.switchTheme(theme)
        // Перезапустить активность для применения новой темы
    }
}
