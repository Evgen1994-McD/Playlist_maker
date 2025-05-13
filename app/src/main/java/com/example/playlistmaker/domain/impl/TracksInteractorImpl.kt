package com.example.playlistmaker.domain.impl

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat.startActivity
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.activity.MediaActivity
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


    override fun getTrackIntentAndStart(track: Track, context: Context) {
        val intent =
            Intent(context, MediaActivity::class.java) // создали интент для перехода на активити
        intent.putExtra("trackName", track.trackName)
        if (!track.collectionName.isNullOrEmpty()) {
            intent.putExtra(
                "collectionName",
                track.collectionName
            )  // отправим альбом только если он есть
        }
        intent.putExtra("trackTimeMillis", track.trackTimeMillis)
        intent.putExtra("artistName", track.artistName)
        intent.putExtra("primaryGenreName", track.primaryGenreName)
        intent.putExtra("country", track.country)
        intent.putExtra("artworkUrl100", track.artworkUrl100)
        intent.putExtra("previewUrl", track.previewUrl)

        intent.putExtra("relieseDate", track.releaseDate)
context.startActivity(intent)
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

    companion object{

        private val debounceIntervalMillis = 1000L // Интервал блокировки в миллисекундах
        private var lastClickTime = System.currentTimeMillis() // Хранение последнего времени клика

    }
}

