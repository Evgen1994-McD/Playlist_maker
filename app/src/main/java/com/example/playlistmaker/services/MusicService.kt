package com.example.playlistmaker.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

internal class MusicService: Service() {

private lateinit var mediaPlayer:MediaPlayer
private lateinit var previewUrl: String

private val binder = MusicServiceBinder()

    inner class MusicServiceBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }
    override fun onBind(intent: Intent?): IBinder?{
        previewUrl = intent?.getStringExtra("track") ?: ""
        preparePlayer(previewUrl)
        return binder
    }


    override fun onUnbind(intent: Intent?): Boolean {
       releasePlayer()
        return super.onUnbind(intent)




    }

    companion object {


        const val STATE_IDLE = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
    }

    private var playerState = STATE_IDLE


    override fun onCreate() {
        super.onCreate()
mediaPlayer = MediaPlayer()

    }

//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        previewUrl = intent?.getStringExtra("track") ?: ""
//        preparePlayer(previewUrl)
//        return Service.START_NOT_STICKY
//    }



     fun stopPlayerAndReset(){
        if (mediaPlayer.isPlaying){
            mediaPlayer.stop()
        }
        mediaPlayer.reset()
    }




     fun preparePlayer(previewUrl: String) {
         if (previewUrl.isEmpty()) return
        if (playerState == STATE_IDLE) {
            try {
                mediaPlayer?.reset()
                mediaPlayer?.setDataSource(previewUrl)
                mediaPlayer?.prepareAsync()
                mediaPlayer?.setOnPreparedListener {
                    Log.d("My_Log", "Media Player prepared")
                }
                mediaPlayer?.setOnCompletionListener {
                    Log.d("MyLog", "Playback completed")
                }
                playerState = STATE_PREPARED
                Log.d("MyLog", "Плеер готов")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

     fun startPlayback() {
        if (playerState != STATE_PREPARED && playerState != STATE_PAUSED) return
        mediaPlayer.start()
        playerState = STATE_PLAYING
        Log.d("MyLog", "Плеер играет")

    }

     fun pausePlayback() {
        if (playerState != STATE_PLAYING) return
        mediaPlayer.pause()
        playerState = STATE_PAUSED
        Log.d("MyLog", "Плеер на Паузе")
    }

     fun releasePlayer() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
        mediaPlayer.release()
        playerState = STATE_IDLE
        Log.d("MyLog", "Плеер освобождён")
    }



    fun updateProgress(): String {
        if (playerState == STATE_PLAYING || playerState == STATE_PAUSED) {
            val formattedTime =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            return formattedTime
        }else return "00:00"
    }



}