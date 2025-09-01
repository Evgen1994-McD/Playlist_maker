package com.example.playlistmaker.ui.media.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import com.example.playlistmaker.utils.TimeUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistTracksViewModel(private val playlistInteractor: PlaylistInteractor,
    private val playListId: Int) : ViewModel() {

    private val playListTracksLiveData1 = MutableLiveData<PlayListTracksScreenState>()
    val getPlaylistTracks1LiveData : LiveData<PlayListTracksScreenState> get() = playListTracksLiveData1
init {
}




   private fun tracksTimeInMinutes(list: List<Track>):String{
       var summTrackTimeMillis: Long = 0
       list.forEach { track->
           summTrackTimeMillis +=(TimeUtils.parseTrackTime(track.trackTimeMillis)).toLong()
       }
       var formattedTimeInString = TimeUtils.finalTracksTime(summTrackTimeMillis)

       return formattedTimeInString

    }

    fun deleteTrackOfPlaylistById(trackId: String)=viewModelScope.launch{
        playlistInteractor.deleteTrackOfPlaylistById(trackId)

    }

    fun getPlayListState()= viewModelScope.launch{

        try {


            val currentPlayList = playlistInteractor.getPlaylistById(playListId)
            val trackList = playlistInteractor.getTrackOfPlaylistById(currentPlayList.tracksId)
            val timeForScreenState = tracksTimeInMinutes(trackList)
            playListTracksLiveData1.postValue(
                PlayListTracksScreenState(
                    name = currentPlayList.name,
                    title = currentPlayList.about,
                    time = timeForScreenState,
                    size = currentPlayList.size,
                    image = currentPlayList.image.toString(),
                    tracks = trackList
                )
            )
        }
        catch (e:Exception){
            Log.d("get", e.message.toString())
        }

    }

}