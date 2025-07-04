package com.example.playlistmaker.ui.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase

class MediaFragmentViewModel(): ViewModel() {
    // Переменная LiveData хранит позицию активной вкладки
    private val _currentTabPosition = MutableLiveData<Int>()
    val currentTabPosition: LiveData<Int> = _currentTabPosition

    // Функция для установки новой позиции вкладки
    fun setCurrentTabPosition(newPosition: Int) {
        _currentTabPosition.value = newPosition
    }


}