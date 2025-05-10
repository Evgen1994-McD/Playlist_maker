package com.example.playlistmaker.domain.api

import android.content.Context

interface OpenUrlInDefaultBrowserInteractor {
    fun openUrlInDefaultBrowser(context: Context, url: String)
}