package com.example.playlistmaker.ui.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.playlists.PlaylistInteractor

class PlaylistFragmentViewModel(private val playlistInteractor: PlaylistInteractor):ViewModel() {

    private val allPlayListsData = MutableLiveData<List<PlayList>>()

    val getLiveData : LiveData<List<PlayList>> get() = allPlayListsData


    suspend fun getAllPlaylists(){
     allPlayListsData.value =  playlistInteractor.getAllPlayList()
    }


}