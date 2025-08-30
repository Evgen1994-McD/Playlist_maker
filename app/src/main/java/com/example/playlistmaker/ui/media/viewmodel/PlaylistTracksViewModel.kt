package com.example.playlistmaker.ui.media.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import com.example.playlistmaker.utils.TimeUtils
import kotlinx.coroutines.launch

class PlaylistTracksViewModel(private val playlistInteractor: PlaylistInteractor,
    private val playListId: Int) : ViewModel() {
        private val currentPlayList = playlistInteractor.

    private val playListTracksLiveData = MutableLiveData<List<Track>>()
    private val timeLiveData = MutableLiveData<String>()


    val getTimeLiveData : LiveData<String> get() = timeLiveData
    val getPlaylistTracksLiveData : LiveData<List<Track>> get() = playListTracksLiveData



    private val playListTracksLiveData1 = MutableLiveData<PlayListTracksScreenState>()
    val getPlaylistTracks1LiveData : LiveData<PlayListTracksScreenState> get() = playListTracksLiveData1



    fun getTracksOfPlaylist(ids: String)=viewModelScope.launch{
        val trackList = playlistInteractor.getTrackOfPlaylistById(ids)


        playListTracksLiveData.value = trackList
        tracksTimeInMinutes(trackList)
    }

   private fun tracksTimeInMinutes(list: List<Track>){
       var summTrackTimeMillis: Long = 0
       list.forEach { track->
           summTrackTimeMillis +=(TimeUtils.parseTrackTime(track.trackTimeMillis)).toLong()
       }
       var formattedTimeInString = TimeUtils.finalTracksTime(summTrackTimeMillis)
       timeLiveData.value = formattedTimeInString

    }

    fun deleteTrackOfPlaylistById(trackId: String)=viewModelScope.launch{
        playlistInteractor.deleteTrackOfPlaylistById(trackId)


    }

}