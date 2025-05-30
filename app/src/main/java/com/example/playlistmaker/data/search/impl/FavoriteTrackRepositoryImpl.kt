package com.example.playlistmaker.data.search.impl

import android.content.Context
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.FavoriteTrackRepository
import com.example.playlistmaker.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoriteTrackRepositoryImpl(private val context: Context) : FavoriteTrackRepository {
    companion object {
        const val PREFS_NAME = Constants.TRACK_STORAGE_PREFS_NAME
        const val TRACKS_KEY = Constants.TRACK_STORAGE_TRACKS_KEY
    }

    private val gson = Gson()
    private var tracks = mutableListOf<TrackDto>() // Список для хранения треков


    init {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val tracksJson = prefs.getString(TRACKS_KEY, null)
        if (!tracksJson.isNullOrBlank()) {
            val type = object : TypeToken<List<TrackDto>>() {}.type
            tracks = gson.fromJson(tracksJson, type) ?: mutableListOf()
        }


    }



    override fun addTrack(track: Track) {
        if (tracks.removeIf { it.trackId == track.trackId }) {

            // Добавляем новый трек в начало списка
            tracks.add(0, TrackDto(
                trackName = track.trackName,
                artistName = track.artistName,
                trackTimeMillis = track.trackTimeMillis,
                artworkUrl100 = track.artworkUrl100,
                trackId = track.trackId,
                collectionName = track.collectionName,
                releaseDate = track.releaseDate,
                primaryGenreName = track.primaryGenreName,
                country = track.country,
                previewUrl = track.previewUrl
            )
            )
        } else {
            // Добавляем новый трек
            if (tracks.size >= 10) {
                tracks.removeAt(tracks.lastIndex) // Удаляем последний трек
            }
            tracks.add(0, TrackDto(
                trackName = track.trackName,
                artistName = track.artistName,
                trackTimeMillis = track.trackTimeMillis,
                artworkUrl100 = track.artworkUrl100,
                trackId = track.trackId,
                collectionName = track.collectionName,
                releaseDate = track.releaseDate,
                primaryGenreName = track.primaryGenreName,
                country = track.country,
                previewUrl = track.previewUrl
            )
            ) // Добавляем новый трек в начало
        }
        // Ограничиваем количество треков до 10
        if (tracks.size >= 10) {
            tracks = tracks.takeLast(10).toMutableList()
        }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val tracksJson = gson.toJson(tracks)
        editor.putString(TRACKS_KEY, tracksJson)
        editor.apply() // Сохраняем изменения в SharedPreferences
    }

    override fun getAllTracks(): List<Track> {
        return tracks.map {
            Track(
                trackName = it.trackName,
                artistName = it.artistName,
                trackTimeMillis = it.trackTimeMillis, // преобразую и пеоедам время сразу
                artworkUrl100 = it.artworkUrl100,
                trackId = it.trackId,
                collectionName = it.collectionName,
                releaseDate = it.releaseDate,
                primaryGenreName = it.primaryGenreName,
                country = it.country,
                previewUrl = it.previewUrl

            )
        }
    }





    override fun clearHistory() {
        tracks.clear() // Метод теперь очищает список треков
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val tracksJson = gson.toJson(tracks)
        editor.putString(TRACKS_KEY, tracksJson)
        editor.apply() // сохраняет очищенный список треков
    }




}