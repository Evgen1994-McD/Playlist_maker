package com.example.playlistmaker.domain.settings.impl

import android.content.Context
import com.example.playlistmaker.domain.settings.OpenUrlUseCase
import com.example.playlistmaker.domain.settings.SettingsRepository

class OpenUrlUseCaseImpl(private val repository: SettingsRepository) : OpenUrlUseCase {
    override fun openUrlInDefaultBrowser(context: Context, url: String) {
        val intent = repository.openUrlInDefaultBrowser(url)
        context.startActivity(intent)
    }
}