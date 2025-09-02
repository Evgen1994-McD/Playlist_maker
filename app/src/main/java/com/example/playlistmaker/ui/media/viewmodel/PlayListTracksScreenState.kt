package com.example.playlistmaker.ui.media.viewmodel

import android.R
import com.example.playlistmaker.domain.models.Track

data  class PlayListTracksScreenState(
        val name: String="",
        val title: String="",
        val time: String="",
        val size: Int=0,
        val image: String="",
        val tracks: List<Track> = emptyList(),
        val listId :Int?
) {






}