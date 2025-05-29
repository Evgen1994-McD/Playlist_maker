package com.example.playlistmaker.domain.settings

import android.content.Context

interface OpenUrlUseCase {
    fun openUrlInDefaultBrowser(context: Context, url: String)
}