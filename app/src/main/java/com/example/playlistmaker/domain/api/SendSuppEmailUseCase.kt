package com.example.playlistmaker.domain.api

import android.content.Context

interface SendSuppEmailUseCase {
    fun sendSuppEmail(context: Context, myEmail: String, subject: String, body: String)
}