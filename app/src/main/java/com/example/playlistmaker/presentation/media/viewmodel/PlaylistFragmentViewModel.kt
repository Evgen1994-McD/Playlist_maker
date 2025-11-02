package com.example.playlistmaker.presentation.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import kotlinx.coroutines.launch

class PlaylistFragmentViewModel(private val playlistInteractor: PlaylistInteractor):ViewModel() {

    private val allPlayListsData = MutableLiveData<List<PlayList>>()

    val getLiveData : LiveData<List<PlayList>> get() = allPlayListsData


     fun getAllPlaylists() = viewModelScope.launch{
     allPlayListsData.value =  playlistInteractor.getAllPlayList()
    }



}