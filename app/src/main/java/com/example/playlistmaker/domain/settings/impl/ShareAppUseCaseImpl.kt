package com.example.playlistmaker.domain.settings.impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.ShareAppUseCase

class ShareAppUseCaseImpl(private val repository: SettingsRepository) : ShareAppUseCase {
    override fun shareApp(context: Context) {
        context.startActivity(
            Intent.createChooser(
                repository.shareApp(),
                context.getString(R.string.share_stroke)
            )
        )
    }
}