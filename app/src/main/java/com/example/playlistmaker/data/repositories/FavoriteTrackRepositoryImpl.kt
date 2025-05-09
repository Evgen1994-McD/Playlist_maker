package com.example.playlistmaker.data.repositories

import android.content.Context
import com.example.playlistmaker.data.Constants
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.api.FavoriteTrackRepository
import com.example.playlistmaker.domain.models.Track
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
        loadTracksFromPrefs() // Загрузим треки при создании объекта
    }

   override fun addTrack(track: Track) {
           if( tracks.removeIf{ it.trackId == track.trackId }) {

            // Добавляем новый трек в начало списка
            tracks.add(0, createTrackDtoFromTrack(track))
        }

         else {
            // Добавляем новый трек
               if (tracks.size >= 10) {
                   tracks.removeAt(tracks.lastIndex) // Удаляем последний трек
               }
               tracks.add(0, createTrackDtoFromTrack(track)) // Добавляем новый трек в начало
        }
        // Ограничиваем количество треков до 10
        if (tracks.size >= 10) {
            tracks = tracks.takeLast(10).toMutableList()
        }
        saveTracksToPrefs() // Сохраняем изменения в SharedPreferences
    }
    override fun getAllTracks(): List<Track> {
        return tracks.map {
            createTrackFromTrackDto(it)
        }
    }
    // Метод для загрузки треков из SharedPreferences
    override fun loadTracksFromPrefs() : List<TrackDto> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val tracksJson = prefs.getString(TRACKS_KEY, null)
        if (!tracksJson.isNullOrBlank()) {
            val type = object : TypeToken<List<TrackDto>>() {}.type
            tracks = gson.fromJson(tracksJson, type) ?: mutableListOf()

        }
        return tracks
    }

    // Метод для сохранения треков в SharedPreferences
    override fun saveTracksToPrefs() {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val tracksJson = gson.toJson(tracks)
        editor.putString(TRACKS_KEY, tracksJson)
        editor.apply()
    }

    override fun clearHistory(){
       tracks.clear() // Метод теперь очищает список треков
        saveTracksToPrefs() // сохраняет очищенный список треков
        }

    override fun createTrackDtoFromTrack(track: Track) : TrackDto {
        return TrackDto(
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
    }
        override fun createTrackFromTrackDto(trackDto: TrackDto) : Track {
            return Track(
                trackName = trackDto.trackName,
                artistName = trackDto.artistName,
                trackTimeMillis = trackDto.trackTimeMillis,
                artworkUrl100 = trackDto.artworkUrl100,
                trackId = trackDto.trackId,
                collectionName = trackDto.collectionName,
                releaseDate = trackDto.releaseDate,
                primaryGenreName = trackDto.primaryGenreName,
                country = trackDto.country,
                previewUrl = trackDto.previewUrl

            )
        }



            }