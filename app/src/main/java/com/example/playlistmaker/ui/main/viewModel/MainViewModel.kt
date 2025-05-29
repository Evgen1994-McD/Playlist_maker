package com.example.playlistmaker.ui.main.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase
import com.example.playlistmaker.ui.search.viewModel.SearchViewModel

class MainViewModel(private val switchThemeUseCase: SwitchThemeUseCase): ViewModel() {

    fun controlThemeInOtherWindows(){
        switchThemeUseCase.controlThemeInOtherWindows()
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