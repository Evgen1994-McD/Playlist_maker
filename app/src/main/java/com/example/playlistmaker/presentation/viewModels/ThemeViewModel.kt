package com.example.playlistmaker.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.SwitchThemeUseCase

class ThemeViewModel(private val switchThemeUseCase: SwitchThemeUseCase) : ViewModel() {




 var currentThemeLiveData = MutableLiveData<Boolean>()

    fun loadingLiveData() : LiveData<Boolean> = currentThemeLiveData

    init {

       currentThemeLiveData.value = switchThemeUseCase.controlThemeInOtherWindows()
    }



    fun controlThemeInOtherWindows(){
        switchThemeUseCase.controlThemeInOtherWindows()
    }

    fun controlTHemeBySwitcher(theme : Boolean){
        switchThemeUseCase.switchThemeModeBySettings(theme)

    }


    companion object{

        fun getViewModelFactory() : ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ThemeViewModel(
                        Creator.provideSwitchThemeUseCase()

                    ) as T
                }

            }
    }


}