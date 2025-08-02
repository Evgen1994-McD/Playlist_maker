package com.example.playlistmaker.ui.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.db.MainDb
import com.example.playlistmaker.data.db.converters.TrackDbConvertor
import com.example.playlistmaker.domain.db.FavoriteRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class FavoriteFragmentViewModel(
    private val favoriteRepository: FavoriteRepository

):ViewModel() {



    val favoriteTracks: LiveData<List<Track>> get() = favoriteRepository.getFavoriteTracks()
        .asLiveData()
/*
Это временная лайв дата чтобы отобразить плейсхолдеры, потом переделаю
при появлении задания на реализацию логики.
 */


}