package com.example.playlistmaker.domain.settings

import android.content.Intent

interface SettingsRepository {
    fun shareApp(): Intent


    fun sendSuppEmail(myEmail: String, subject: String, body: String): Intent
    fun openUrlInDefaultBrowser(url: String): Intent
    fun controlAppThemeMode(): Boolean
    fun saveCurrentThemeToShared(boolean: Boolean)
    fun switchTheme(savedTheme: Boolean)
}