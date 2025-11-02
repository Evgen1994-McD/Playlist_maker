package com.example.playlistmaker.presentation.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.playlistmaker.domain.db.FavoriteRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.map

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