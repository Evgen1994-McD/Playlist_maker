package com.example.playlistmaker.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.domain.api.SwitchThemeUseCase

class MainViewModel(private val switchThemeUseCase: SwitchThemeUseCase) : ViewModel() {




 var currentThemeLiveData = MutableLiveData<Boolean>()

    fun loadingLiveData() : LiveData<Boolean> = currentThemeLiveData

    init {

       currentThemeLiveData.value = switchThemeUseCase.controlThemeInOtherWindows()
    }


    companion object{

        fun getViewModelFactory() : ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(
                        Creator.provideSwitchThemeUseCase()

                    ) as T
                }

            }
    }


}