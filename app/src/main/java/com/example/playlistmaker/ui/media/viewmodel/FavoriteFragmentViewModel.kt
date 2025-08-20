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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.Dispatcher

class FavoriteFragmentViewModel(
    private val favoriteRepository: FavoriteRepository

):ViewModel() {



    val favoriteTracks: LiveData<List<Track>>
        get() = favoriteRepository.getFavoriteTracks()
            // Преобразуем каждый элемент списка Tracks с новым значением artworkUrl100
            .map { tracks ->
                tracks.map { track ->
                    track.copy(
                        artworkUrl100 = getCoverArtwork(track.artworkUrl100).toString()
                    )
                }
            }.asLiveData()


    fun getCoverArtwork(artworkUrl100: String) =
        artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")


}