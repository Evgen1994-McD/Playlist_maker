package com.example.playlistmaker.domain.api

import android.content.Context
import android.content.Intent

interface SettingsRepository {
    fun shareApp(): Intent

    fun sendSuppEmail(myEmail: String, subject: String, body: String): Intent
    fun openUrlInDefaultBrowser(url: String): Intent
    fun controlAppThemeMode(applicationContext: Context): Boolean
    fun saveCurrentThemeToShared(boolean: Boolean)
    fun switchTheme(savedTheme: Boolean)
}