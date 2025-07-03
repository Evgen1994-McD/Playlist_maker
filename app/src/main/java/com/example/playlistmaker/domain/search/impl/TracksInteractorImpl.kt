package com.example.playlistmaker.domain.search.impl

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.Runnable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class TracksInteractorImpl(
    private val repository: TrackRepository,
    private val handler: Handler = Handler(
        Looper.getMainLooper()
    ),
    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
) : TrackInteractor, Runnable {
    private var currentTask: Future<*>? = null

    override fun searchTracks(expression: String, consumer: TrackInteractor.TracksConsumer) {
        handler.removeCallbacksAndMessages(null)
        currentTask?.cancel(true)
        currentTask = executor.submit(Runnable {
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

    override fun getTrackIntentAndStart(
        track: Track,
        context: Context
    ) {
        TODO("Not yet implemented")
    }


    override fun clickDebounce(): Boolean {

        val now = System.currentTimeMillis()

        // Проверяем прошло ли достаточно времени с момента последнего клика
        if (now - lastClickTime >= debounceIntervalMillis) {
            lastClickTime = now // Обновляем время последнего клика
            return true // Клик разрешен
        }
        return false // Клик запрещен
    }

    override fun run() {

    }

    companion object {

        private val debounceIntervalMillis = 10L // метод тут вроде Не нужен, наверное лучше убрать
        private var lastClickTime = System.currentTimeMillis() // Хранение последнего времени клика

    }
}

