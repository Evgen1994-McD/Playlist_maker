package com.example.playlistmaker.ui.media.viewmodel

import android.R
import com.example.playlistmaker.domain.models.Track

sealed  class PlayListTracksScreenState {

        data class PlaylistName(val name: String) : PlayListTracksScreenState()
        data class PlaylistTitle(val title: String) : PlayListTracksScreenState()
        data class PlaylistTime(val time: String) : PlayListTracksScreenState()
        data class PlaylistSize(val size: String) : PlayListTracksScreenState()
        data class PlaylistImame(val image: String) : PlayListTracksScreenState()
        data class PlaylistTrackList(val tracks: List<Track>) : PlayListTracksScreenState()




}