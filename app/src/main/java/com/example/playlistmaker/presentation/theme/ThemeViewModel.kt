package com.example.playlistmaker.presentation.theme

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase

class ThemeViewModel(
    private val switchThemeUseCase: SwitchThemeUseCase
) : ViewModel() {

    private val currentThemeLiveData = MutableLiveData<Boolean>()
    
    init {
        currentThemeLiveData.value = switchThemeUseCase.controlThemeInOtherWindows()
    }
    
    val themeMode: LiveData<Boolean> get() = currentThemeLiveData

    fun updateTheme(theme: Boolean) {
        switchThemeUseCase.switchThemeModeBySettings(theme)
        currentThemeLiveData.value = theme
    }
}

