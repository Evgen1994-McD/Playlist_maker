package com.example.playlistmaker.presentation.settings.viewModel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.example.playlistmaker.domain.settings.OpenUrlUseCase
import com.example.playlistmaker.domain.settings.SendSuppEmailUseCase
import com.example.playlistmaker.domain.settings.ShareAppUseCase
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase


class SettingsViewModel(
    private val switchThemeUseCase: SwitchThemeUseCase,
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


   private var currentThemeLiveData = MutableLiveData<Boolean>()


    init {

        currentThemeLiveData.value = switchThemeUseCase.controlThemeInOtherWindows()

    }

    val getLiveData: LiveData<Boolean> get() = currentThemeLiveData


    fun controlTHemeBySwitcher(theme: Boolean) {
        switchThemeUseCase.switchThemeModeBySettings(theme)

    }

}




