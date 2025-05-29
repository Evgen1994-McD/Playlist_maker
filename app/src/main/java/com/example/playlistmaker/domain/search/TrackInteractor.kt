package com.example.playlistmaker.domain.search

import android.content.Context
import com.example.playlistmaker.domain.models.Track

interface TrackInteractor {

    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>)
        fun onFailure(error: Throwable)
    }

    fun getTrackIntentAndStart(track: Track, context: Context) // получим интент при нажатии на трек

    // ну и запустим( далее)
    fun clickDebounce(): Boolean

}
