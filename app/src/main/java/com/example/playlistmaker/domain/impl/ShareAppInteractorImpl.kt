package com.example.playlistmaker.domain.impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.R
import com.example.playlistmaker.data.repositories.SettingsReposytoryImpl
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.api.ShareAppInteractor

class ShareAppInteractorImpl(private val repository: SettingsRepository) : ShareAppInteractor {
    override fun shareApp(context: Context) {
        context.startActivity(
            Intent.createChooser(
                repository.shareApp(context),
                context.getString(R.string.share_stroke)
            ))
    }
}