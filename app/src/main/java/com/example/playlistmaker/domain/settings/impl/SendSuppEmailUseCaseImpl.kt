package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.SendSuppEmailUseCase
import com.example.playlistmaker.domain.settings.SettingsRepository

class SendSuppEmailUseCaseImpl(private val repository: SettingsRepository) : SendSuppEmailUseCase {
    override fun sendSuppEmail() {
        repository.sendSuppEmail()

    }
}