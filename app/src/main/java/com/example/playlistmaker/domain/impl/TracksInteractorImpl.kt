package com.example.playlistmaker.domain.impl

import android.R.attr.query
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

class TracksInteractorImpl (private val repository: TrackRepository) : TrackInteractor, CoroutineScope by MainScope() {
private var currentJob : Job? = null
        private val executor = Executors.newCachedThreadPool()
    private val handler =
        Handler(Looper.getMainLooper()) // хендлер - логику дебаунс поиска сделать тут?
        @OptIn(FlowPreview::class)
        override fun searchTracks(expression: String, consumer: TrackInteractor.TracksConsumer) {
 //executor.execute {
    currentJob = launch(Dispatchers.IO) {
        flowOf(query)
            .filterNotNull()
            .debounce(10000L)
            .collectLatest { q ->
                withContext(Dispatchers.Main) {
//...b
                }

                try {

                    consumer.consume(repository.searchTracks(q.toString()))

                } catch (ex: Exception) {
                    consumer.onFailure(ex)
                }
            }
    }
        }




          //  }
        }

