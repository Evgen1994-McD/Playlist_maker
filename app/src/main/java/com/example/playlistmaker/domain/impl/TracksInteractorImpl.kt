package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import java.util.concurrent.Executors

class TracksInteractorImpl (private val repository: TrackRepository) : TrackInteractor {

        private val executor = Executors.newCachedThreadPool()

        override fun searchTracks(expression: String, consumer: TrackInteractor.TracksConsumer) {
            executor.execute {
                consumer.consume(repository.searchTracks(expression))
            }
        }
    }
