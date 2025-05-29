package com.example.playlistmaker.domain.settings.impl

import android.content.Context
import com.example.playlistmaker.domain.settings.SendSuppEmailUseCase
import com.example.playlistmaker.domain.settings.SettingsRepository

class SendSuppEmailUseCaseImpl(private val repository: SettingsRepository) : SendSuppEmailUseCase {
    override fun sendSuppEmail(context: Context, myEmail: String, subject: String, body: String) {
        val intent = repository.sendSuppEmail(myEmail, subject, body)
        context.startActivity(intent)
    }
}