package com.example.playlistmaker.domain.api

import android.content.Context

interface SettingsInteractor {
    fun shareApp(context: Context)
    fun sendSuppEmail(context: Context, myEmail: String, subject : String, body : String)
    fun openUrlInDefaultBrowser(context: Context, url: String)
}