package com.example.playlistmaker.domain.settings



interface SettingsRepository {
    fun shareApp()


    fun sendSuppEmail()
    fun openUrlInDefaultBrowser()
    fun controlAppThemeMode(): Boolean
    fun saveCurrentThemeToShared(boolean: Boolean)
    fun switchTheme(savedTheme: Boolean)
}