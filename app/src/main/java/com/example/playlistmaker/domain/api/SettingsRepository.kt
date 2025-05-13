package com.example.playlistmaker.domain.api

import android.content.Context
import android.content.Intent

interface SettingsRepository {
    fun shareApp(context: Context): Intent

    fun sendSuppEmail(myEmail: String, subject: String, body: String): Intent
    fun openUrlInDefaultBrowser(url: String): Intent
    fun controlAppThemeMode(applicationContext: Context, context: Context): Boolean
    fun saveCurrentThemeToShared(context: Context, boolean: Boolean)
}