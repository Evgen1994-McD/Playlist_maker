package com.example.playlistmaker.ui.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(private val playlistInteractor: PlaylistInteractor):ViewModel() {

    private val allPlayListsData = MutableLiveData<List<PlayList>>()
    private val playListTracksLiveData = MutableLiveData<List<Track>>()

    val getLiveData : LiveData<List<PlayList>> get() = allPlayListsData
    val getPlaylistTracksLiveData : LiveData<List<Track>> get() = playListTracksLiveData


     fun getAllPlaylists() = viewModelScope.launch{
     allPlayListsData.value =  playlistInteractor.getAllPlayList()
    }

    fun getTracksOfPlaylist(ids: String)=viewModelScope.launch{
        playListTracksLiveData.value = playlistInteractor.getTrackOfPlaylistById(ids)
    }


}