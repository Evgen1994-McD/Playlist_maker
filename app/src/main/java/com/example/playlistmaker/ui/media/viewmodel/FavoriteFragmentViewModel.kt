package com.example.playlistmaker.ui.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase

class FavoriteFragmentViewModel(private val switchThemeUseCase: SwitchThemeUseCase):ViewModel() {
    fun controlThemeInOtherWindows(){
        switchThemeUseCase.controlThemeInOtherWindows()
    }

    private val favoriteTrackList = MutableLiveData(false)

    val getLiveData : MutableLiveData<Boolean> get() = favoriteTrackList
/*
Это временная лайв дата чтобы отобразить плейсхолдеры, потом переделаю
при появлении задания на реализацию логики
 */


}