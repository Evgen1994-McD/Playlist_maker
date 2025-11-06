package com.example.playlistmaker.presentation.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import kotlinx.coroutines.launch

class ReplacePlaylistViewModel(private val playlistInteractor: PlaylistInteractor,
    private val listId:Int) :
    AddPlayListViewModel(playlistInteractor) {

        val mutableLiveData = MutableLiveData<PlayList>()
    val getLiveData : LiveData<PlayList> get() = mutableLiveData


    fun getReplacePlaylist()=viewModelScope.launch{
        mutableLiveData.value = playlistInteractor.getPlaylistById(listId)
    }



}