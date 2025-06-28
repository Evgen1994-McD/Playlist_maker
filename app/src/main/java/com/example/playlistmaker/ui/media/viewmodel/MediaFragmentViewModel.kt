package com.example.playlistmaker.ui.media.viewmodel

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase

class MediaFragmentViewModel(private val switchThemeUseCase: SwitchThemeUseCase): ViewModel() {

    fun controlThemeInOtherWindows() {
        switchThemeUseCase.controlThemeInOtherWindows()
    }

}