package com.example.playlistmaker.domain.impl

import android.content.Context
import com.example.playlistmaker.data.repositories.SettingsReposytoryImpl
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.api.ShareAppInteractor

class ShareAppInteractorImpl(private val repository: SettingsRepository):ShareAppInteractor {
    override fun shareApp(context: Context) {
        repository.shareApp(context)
    }
}