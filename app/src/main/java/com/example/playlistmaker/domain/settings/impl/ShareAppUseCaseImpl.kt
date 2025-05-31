package com.example.playlistmaker.domain.settings.impl


import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.ShareAppUseCase

class ShareAppUseCaseImpl(private val repository: SettingsRepository) : ShareAppUseCase {
    override fun shareApp() {
        repository.shareApp()


    }
}