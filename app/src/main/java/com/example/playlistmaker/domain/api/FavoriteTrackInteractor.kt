package com.example.playlistmaker.domain.api

import android.content.SharedPreferences
import com.example.playlistmaker.domain.models.Track

interface FavoriteTrackInteractor {

    fun clearHistory()
    fun addTrack(track: Track)
    fun getAllTracksFromStorage(): List<Track>


    fun registerOnSharedPrefsChanger(listenre: SharedPreferences.OnSharedPreferenceChangeListener)
}