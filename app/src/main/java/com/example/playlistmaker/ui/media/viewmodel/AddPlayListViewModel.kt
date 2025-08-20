package com.example.playlistmaker.ui.media.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import kotlinx.coroutines.launch

class AddPlayListViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {


    fun savePlayList(playList: PlayList) = viewModelScope.launch{
        playlistInteractor.insertPlayList(playList)
    }


}