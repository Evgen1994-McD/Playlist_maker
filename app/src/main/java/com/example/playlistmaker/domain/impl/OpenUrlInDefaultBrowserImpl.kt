package com.example.playlistmaker.domain.impl

import android.content.Context
import com.example.playlistmaker.domain.api.OpenUrlInDefaultBrowserInteractor
import com.example.playlistmaker.domain.api.SettingsRepository

class OpenUrlInDefaultBrowserImpl (private val repository: SettingsRepository): OpenUrlInDefaultBrowserInteractor {
   override fun openUrlInDefaultBrowser(context: Context, url: String) {
    val intent = repository.openUrlInDefaultBrowser(url)
    context.startActivity(intent)
}
}