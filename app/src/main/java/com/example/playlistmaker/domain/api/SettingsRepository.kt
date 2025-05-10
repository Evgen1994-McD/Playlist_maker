package com.example.playlistmaker.domain.api

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.App

interface SettingsRepository {
    fun shareApp(context: Context) : Intent

     fun sendSuppEmail( myEmail: String, subject : String, body : String) : Intent
    fun openUrlInDefaultBrowser(url: String) : Intent
    fun controlAppThemeMode(applicationContext: Context, context: Context, sharedPreferences: SharedPreferences) : Boolean
}