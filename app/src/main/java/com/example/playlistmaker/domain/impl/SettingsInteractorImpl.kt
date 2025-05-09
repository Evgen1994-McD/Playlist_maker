package com.example.playlistmaker.domain.impl

import android.content.Context
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.data.repositories.SettingsReposytoryImpl
import com.example.playlistmaker.domain.api.SettingsInteractor
import com.example.playlistmaker.domain.api.SettingsRepository

class SettingsInteractorImpl( private val repository: SettingsRepository) : SettingsInteractor {
    override fun shareApp(context: Context) {
        repository.shareApp(context)
    }

    override fun sendSuppEmail(context: Context, myEmail: String, subject : String, body : String) {
      val intent = repository.sendSuppEmail(myEmail, subject, body)
        context.startActivity(intent)


    }

    override fun openUrlInDefaultBrowser(context: Context, url: String) {
      val intent = repository.openUrlInDefaultBrowser(url)
        context.startActivity(intent)
    }
}