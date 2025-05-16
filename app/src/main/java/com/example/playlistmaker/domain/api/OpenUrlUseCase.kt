package com.example.playlistmaker.domain.api

import android.content.Context

interface OpenUrlUseCase {
    fun openUrlInDefaultBrowser(context: Context, url: String)
}