package com.example.playlistmaker.domain.impl

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import com.example.playlistmaker.data.Constants
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.api.SwitchThemeInteractor
import com.google.android.material.switchmaterial.SwitchMaterial

class SwitchThemeInteractorImpl(private val repository: SettingsRepository) :
    SwitchThemeInteractor {
    override fun switchThemeModeBySettings(
        switch: SwitchMaterial?,
        applicationContext: Context,
        context: Context,
        sharedPrefs: SharedPreferences,
        activity: Activity
    ) {
        val theme = repository.controlAppThemeMode(applicationContext, context, sharedPrefs)
        switch!!.isChecked = theme

        switch.setOnCheckedChangeListener { _, isChecked ->
            // Сохранить новое значение темы in Sharedpreferences
            sharedPrefs.edit().putBoolean(Constants.KEY_THEME_MODE, isChecked).apply()

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
}