package com.example.playlistmaker.ui.main.viewModel

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase

class MainViewModel(private val switchThemeUseCase: SwitchThemeUseCase): ViewModel() {

    fun controlThemeInOtherWindows(){
        switchThemeUseCase.controlThemeInOtherWindows()
    }



}