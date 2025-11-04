package com.example.playlistmaker.presentation.settings.viewModel

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.OpenUrlUseCase
import com.example.playlistmaker.domain.settings.SendSuppEmailUseCase
import com.example.playlistmaker.domain.settings.ShareAppUseCase

class SettingsViewModel(
    private val shareAppUseCase: ShareAppUseCase,
    private val sendToSuppUse: SendSuppEmailUseCase,
    private val openUriUseCase: OpenUrlUseCase
) : ViewModel() {
    companion object {


    }

    fun shareApp() {  // Метод - интент для отправки сообщений
        shareAppUseCase.shareApp()

    }


    fun openUrlInDefaultBrowser() {

        openUriUseCase.openUrlInDefaultBrowser()
    }


    fun sendSuppEmail() {  // Приватный метод для письма в поддержку


        sendToSuppUse.sendSuppEmail()
    }

}




