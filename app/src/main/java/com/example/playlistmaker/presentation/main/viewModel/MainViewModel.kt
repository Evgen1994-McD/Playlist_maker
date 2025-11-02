package com.example.playlistmaker.presentation.main.viewModel

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase

class MainViewModel(private val switchThemeUseCase: SwitchThemeUseCase): ViewModel() {

    fun controlThemeInOtherWindows(){
        switchThemeUseCase.controlThemeInOtherWindows()
    }



}