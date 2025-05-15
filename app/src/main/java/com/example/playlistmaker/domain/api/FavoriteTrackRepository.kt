package com.example.playlistmaker.domain.api

import android.content.SharedPreferences
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.models.Track

interface FavoriteTrackRepository {
    fun addTrack(track: Track) {}

    fun getAllTracks(): List<Track>



    fun clearHistory()



    fun favoriteSharedListener(listener: SharedPreferences.OnSharedPreferenceChangeListener)
}