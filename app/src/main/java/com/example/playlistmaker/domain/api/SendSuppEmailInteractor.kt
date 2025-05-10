package com.example.playlistmaker.domain.api

import android.content.Context

interface SendSuppEmailInteractor {
    fun sendSuppEmail(context: Context, myEmail: String, subject : String, body : String)
}