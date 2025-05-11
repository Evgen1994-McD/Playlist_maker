package com.example.playlistmaker.domain.impl

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import kotlinx.coroutines.Runnable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class TracksInteractorImpl (private val repository: TrackRepository,
    private val handler: Handler = Handler(
        Looper.getMainLooper()),
                            private val executor: ExecutorService = Executors.newSingleThreadExecutor()) : TrackInteractor, Runnable {
    private var currentTask: Future<*>? = null

        override fun searchTracks(expression: String, consumer: TrackInteractor.TracksConsumer) {
            handler.removeCallbacksAndMessages(null)
currentTask?.cancel(true)
 currentTask = executor.submit(Runnable{
            Thread.sleep(2000L)

                try {
                    val tracks = repository.searchTracks(expression)
                    handler.post {

                       consumer.consume(tracks)
                    }

                } catch (ex: Exception) {
                    handler.post {
                        consumer.onFailure(ex)
                    }
                }
            }
 )

    }







    override fun run() {

    }
}

