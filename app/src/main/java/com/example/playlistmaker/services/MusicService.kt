package com.example.playlistmaker.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.player.viewModel.PlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

private const val NOTIFICATION_CHANNEL_ID = "music_service_channel"
private const val NOTIFICATION_ID = 1
private const val TRACK = "track"
private const val ARTIST = "artistName"
private const val TRACKNAME = "trackName"

internal class MusicService : Service(), AudioPlayerControl {

    private var timerJob: Job? = null
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var previewUrl: String
    private lateinit var artistName: String
    private lateinit var trackName: String

    private val binder = MusicServiceBinder()


    private lateinit var notificationManager: NotificationManager
    private var shouldShowNotification = false

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }

    override fun onBind(intent: Intent?): IBinder? {
        previewUrl = intent?.getStringExtra(TRACK) ?: ""
        artistName = intent?.getStringExtra(ARTIST) ?: ""
        trackName = intent?.getStringExtra(TRACKNAME) ?: ""
        preparePlayer(previewUrl)


        return binder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        releasePlayer()
        return super.onUnbind(intent)


    }


    private val _playerState = MutableStateFlow<PlayerState>(PlayerState.Default())


    // Контроль состояния отображения уведомления
   override fun setShouldShowNotification(show: Boolean) {
        shouldShowNotification = show
        updateNotificationVisibility()
    }

    private fun updateNotificationVisibility() {
        if (shouldShowNotification) {
            startForeground(NOTIFICATION_ID, createServiceNotification())
        } else {
            stopForeground(true)
        }
    }


    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()

    }



    fun preparePlayer(previewUrl: String) {
        if (previewUrl.isEmpty()) return

        try {
            mediaPlayer?.reset()
            mediaPlayer?.setDataSource(previewUrl)
            mediaPlayer?.prepareAsync()
            mediaPlayer?.setOnPreparedListener {
                _playerState.value = PlayerState.Prepared()

            }
            mediaPlayer?.setOnCompletionListener {
                handlePlaybackCompleted()


            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun getPlayerState(): StateFlow<PlayerState> {
        val playerState = _playerState.asStateFlow()
        return playerState
    }

    override fun startPlayback() {
        mediaPlayer.start()
        _playerState.value = PlayerState.Playing(updateProgress())
        startTimer()

    }

    override fun pausePlayback() {
        mediaPlayer.pause()
        _playerState.value = PlayerState.Paused(updateProgress())
        timerJob?.cancel()
    }

    fun releasePlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        timerJob?.cancel()
        mediaPlayer.release()
        _playerState.value = PlayerState.Default()
    }


    fun updateProgress(): String {

        val formattedTime =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        return formattedTime

    }

    private fun startTimer() {
        timerJob = CoroutineScope(Dispatchers.Default).launch {
            while (mediaPlayer?.isPlaying == true) {
                delay(300L)
                _playerState.value = PlayerState.Playing(updateProgress())

            }
        }

    }

    // Обработчик завершения воспроизведения
    private fun handlePlaybackCompleted() {
        // После окончания воспроизведения убиваем таймер и очищаем прогресс
        timerJob?.cancel()
        _playerState.value = PlayerState.Prepared()
        // Устанавливаем флаг, чтобы убрать уведомление
        setShouldShowNotification(false)
    }

    private fun createNotificationChannel() {
        // Создание каналов доступно только с Android 8.0
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val channel = NotificationChannel(
            /* id= */ NOTIFICATION_CHANNEL_ID,
            /* name= */ "Music service",
            /* importance= */ NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = "Service for playing music"

        // Регистрируем канал уведомлений
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createServiceNotification(): Notification {

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(artistName + " - " + trackName)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }



}