package com.example.playlistmaker.domain.settings

import android.content.Context

interface SendSuppEmailUseCase {
    fun sendSuppEmail(context: Context, myEmail: String, subject: String, body: String)
}