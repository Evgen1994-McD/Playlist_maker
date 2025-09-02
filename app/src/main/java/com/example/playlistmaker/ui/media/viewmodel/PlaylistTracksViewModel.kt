package com.example.playlistmaker.ui.media.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import com.example.playlistmaker.utils.TimeUtils
import com.example.playlistmaker.utils.declineNoun
import kotlinx.coroutines.launch

class PlaylistTracksViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playListId: Int
) : ViewModel() {
    private lateinit var currentPlayList: PlayList
    private val playListTracksLiveData1 = MutableLiveData<PlayListTracksScreenState>()
    val getPlaylistTracks1LiveData: LiveData<PlayListTracksScreenState> get() = playListTracksLiveData1

    suspend fun deleteTracksOfDeletedPlayList() {
        val deletedPlaylistTracks = currentPlayList.tracksId.split(",")
        val otherPlaylist =
            currentPlayList.listId?.let { playlistInteractor.selectDontDeletedPlaylists(it) }
        var tempOtherString = ""
        otherPlaylist?.forEach { playlist ->
            tempOtherString += "," + playlist.tracksId
        }
        val splittedTempString = tempOtherString.split(",").toSet()
        var idForDelete =
            deletedPlaylistTracks.filter { element -> !splittedTempString.contains(element) }


        idForDelete.forEach {
            playlistInteractor.deleteTrackOfPlaylistById(it)
        }
        deletePlaylist()

    }

    fun deletePlaylist() = viewModelScope.launch {


        currentPlayList.listId?.let { playlistInteractor.deletePlayListForId(it) }

    }


    fun generateAndSharePlayList(
        playlistName: String,
        title: String,
        tracks: List<Track>,
        context: Context
    ) {
        val tracksCount = declineNoun(
            tracks.size,
            context.getString(R.string.track1),
            context.getString(R.string.track3),
            context.getString(R.string.track2)
        )
        val tracksList = tracks.mapIndexed { index, track ->
            "${index + 1}. ${track.artistName} - ${track.trackName} (${track.trackTimeMillis})"
        }.joinToString("\n")

        val playList = """
        |$playlistName
        |
        |$title
        |
        |$tracksCount 
        |
        |$tracksList
        """.trimMargin()

        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                playList
            ) // Ссылка на курс андроид разработки
            type = "text/plain"
        }


        val chooserIntent = Intent.createChooser(
            intent,
            context.getString(R.string.share_playlist_title)
        )

        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)


    }


    private fun tracksTimeInMinutes(list: List<Track>): String {
        var summTrackTimeMillis: Long = 0
        list.forEach { track ->
            summTrackTimeMillis += (TimeUtils.parseTrackTime(track.trackTimeMillis)).toLong()
        }
        var formattedTimeInString = TimeUtils.finalTracksTime(summTrackTimeMillis)

        return formattedTimeInString

    }

    fun deleteTrackOfPlaylistById(trackId: String) = viewModelScope.launch {
        val currentTracks = currentPlayList.tracksId.split(",")
        val filteredElements = currentTracks.filterNot { it.trim() == trackId.trim() }
        val filteredTracks = filteredElements.joinToString(separator = ",")
        playlistInteractor.deleteTrackOfPlaylistById(trackId)
        playlistInteractor.insertPlayList(
            currentPlayList.copy(
                size = currentPlayList.size - 1,
                tracksId = filteredTracks
            )
        )
        getPlayListState()

    }

    fun getPlayListState() = viewModelScope.launch {

        try {


            currentPlayList = playlistInteractor.getPlaylistById(playListId)
            val trackList = playlistInteractor.getTrackOfPlaylistById(currentPlayList.tracksId)
            val timeForScreenState = tracksTimeInMinutes(trackList)
            playListTracksLiveData1.postValue(
                PlayListTracksScreenState(
                    name = currentPlayList.name,
                    title = currentPlayList.about,
                    time = timeForScreenState,
                    size = currentPlayList.size,
                    image = currentPlayList.image.toString(),
                    tracks = trackList,
                    listId = currentPlayList.listId
                )
            )
        } catch (e: Exception) {
            Log.d("get", e.message.toString())
        }

    }

}