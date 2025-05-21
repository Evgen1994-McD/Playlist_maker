package com.example.playlistmaker.presentation.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.api.SwitchThemeUseCase

class MainViewModel(private val switchThemeUseCase: SwitchThemeUseCase) : ViewModel() {

//    private val switchThemeUseCase by lazy { Creator.provideSwitchThemeUseCase() }


private var currentThemeLiveData = MutableLiveData<Boolean>()

    init {
       currentThemeLiveData.value = switchThemeUseCase.controlThemeInOtherWindows()
    }


    companion object{

        fun getViewModelFactory(switchThemeUseCase: SwitchThemeUseCase) : ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(
                        Creator.provideSwitchThemeUseCase()

                    ) as T
                }

            }
    }


}