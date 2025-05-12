package com.example.playlistmaker.domain.impl

import android.content.Context
import com.example.playlistmaker.domain.api.OpenUrlUseCase
import com.example.playlistmaker.domain.api.SettingsRepository

class OpenUrlUseCaseImpl (private val repository: SettingsRepository): OpenUrlUseCase {
   override fun openUrlInDefaultBrowser(context: Context, url: String) {
    val intent = repository.openUrlInDefaultBrowser(url)
    context.startActivity(intent)
}
}