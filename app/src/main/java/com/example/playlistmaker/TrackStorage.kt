package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.retrofit.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit
import com.example.playlistmaker.utils.Constants

class TrackStorage(private val context: Context) {
    companion object {
        const val PREFS_NAME = Constants.TRACK_STORAGE_PREFS_NAME
        const val TRACKS_KEY = Constants.TRACK_STORAGE_TRACKS_KEY
    }

    private val gson = Gson()
    private var tracks = mutableListOf<Track>() // Список для хранения треков

    init {
        loadTracksFromPrefs() // Загрузим треки при создании объекта
    }

    fun addTrack(track: Track) {
           if( tracks.removeIf{ it.trackId == track.trackId }) {

            // Добавляем новый трек в начало списка
            tracks.add(0, track)
        }

         else {
            // Добавляем новый трек
               if (tracks.size >= 10) {
                   tracks.removeAt(tracks.lastIndex) // Удаляем последний трек
               }
               tracks.add(0, track) // Добавляем новый трек в начало
        }
        // Ограничиваем количество треков до 10
        if (tracks.size >= 10) {
            tracks = tracks.takeLast(10).toMutableList()
        }
        saveTracksToPrefs() // Сохраняем изменения в SharedPreferences
    }
    fun getAllTracks(): List<Track> {
        return tracks
    }
    // Метод для загрузки треков из SharedPreferences
    private fun loadTracksFromPrefs() {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val tracksJson = prefs.getString(TRACKS_KEY, null)
        if (!tracksJson.isNullOrBlank()) {
            val type = object : TypeToken<List<Track>>() {}.type
            tracks = gson.fromJson(tracksJson, type) ?: mutableListOf()
        }
    }

    // Метод для сохранения треков в SharedPreferences
    private fun saveTracksToPrefs() {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val tracksJson = gson.toJson(tracks)
        editor.putString(TRACKS_KEY, tracksJson)
        editor.apply()
    }

    fun clearHistory(){
       tracks.clear() // Метод теперь очищает список треков
        saveTracksToPrefs() // сохраняет очищенный список треков
        }

            }


