package com.example.playlistmaker.domain.settings.impl


import com.example.playlistmaker.domain.settings.OpenUrlUseCase
import com.example.playlistmaker.domain.settings.SettingsRepository

class OpenUrlUseCaseImpl(private val repository: SettingsRepository) : OpenUrlUseCase {
    override fun openUrlInDefaultBrowser() {
        repository.openUrlInDefaultBrowser()

    }
}