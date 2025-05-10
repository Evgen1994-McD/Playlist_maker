package com.example.playlistmaker.domain.impl

import android.content.Context
import com.example.playlistmaker.domain.api.SendSuppEmailInteractor
import com.example.playlistmaker.domain.api.SettingsRepository

class SendSuppEmailInteractorImpl(private val repository: SettingsRepository): SendSuppEmailInteractor {
    override fun sendSuppEmail(context: Context, myEmail: String, subject: String, body: String) {
        val intent = repository.sendSuppEmail(myEmail, subject, body)
        context.startActivity(intent)
    }
}