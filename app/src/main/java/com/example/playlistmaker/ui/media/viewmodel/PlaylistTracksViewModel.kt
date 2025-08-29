package com.example.playlistmaker.ui.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistTracksViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {
    private val playListTracksLiveData = MutableLiveData<List<Track>>()
    val getPlaylistTracksLiveData : LiveData<List<Track>> get() = playListTracksLiveData



    fun getTracksOfPlaylist(ids: String)=viewModelScope.launch{
        playListTracksLiveData.value = playlistInteractor.getTrackOfPlaylistById(ids)
    }

}