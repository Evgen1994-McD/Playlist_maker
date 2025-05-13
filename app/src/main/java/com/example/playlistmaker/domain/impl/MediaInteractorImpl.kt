package com.example.playlistmaker.domain.impl

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat.startActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.data.repositories.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.domain.api.MediaInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.activity.MediaActivity
import com.example.playlistmaker.ui.activity.MediaActivity.Companion
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class MediaInteractorImpl(audioplayer : MediaPlayer) : MediaInteractor {

    private fun View.makeGone() {
        this.visibility = View.GONE // функция для вью гон
    }

    private fun View.makeVisible() {
        this.visibility = View.VISIBLE // функция для вью визибл
    }

    private fun View.makeInvisible() {
        this.visibility = View.INVISIBLE // функция для вью инвизибл
    }
    private lateinit var binding: ActivityMediaBinding // делаю байдинг
    private lateinit var artworkUrl100: String
    private lateinit var storage: FavoriteTrackRepositoryImpl
    private lateinit var collectionName: String
    private lateinit var previewUrl : String
    private var mediaPlayer = MediaPlayer() // делаю медиаплеер
    private var isPlaying = false // переменная статуса плеера

    private val handler =
        Handler(Looper.getMainLooper()) // хэндлер для доступа к главному потоку


    companion object { // компаньон медиаплеера
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val default_time = "00:00"

    }
    private var playerState = STATE_DEFAULT


    override fun preparePlayer() {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener { // слушатель готовности к воспроизведению
            binding.play.isEnabled = true
            playerState = STATE_PREPARED
            startUpdateProgress()
    }
        mediaPlayer.setOnCompletionListener {  //слушатель завершения воспроизведения
            playerState = STATE_PREPARED
            stopUpdateProgress()
            binding.pause.makeInvisible()
            binding.play.makeVisible()
            binding.progressTime.text = default_time
            Log.d("MediaPlayer", "Проигрывание завершено")
        }
        }

        override fun startPlayer() {
            Log.d("MediaPlayer", "стартанули плеер")
            mediaPlayer.start()
            binding.play.makeInvisible()
            binding.pause.makeVisible()
            playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState =STATE_PAUSED
        binding.play.makeVisible()
        binding.pause.makeInvisible()
    }

    override fun playBackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    override fun intentGetExtraBind(intent : Intent, myTracks : List<Track>) {
        if (!intent.getStringExtra("trackName")
                .isNullOrEmpty()
        ) // запускаем интент только если интент есть
        {
            val intent = intent // получаем интент который запустил активность
            val trackName = intent.getStringExtra("trackName")
            previewUrl = intent.getStringExtra("previewUrl").toString()

            val trackTimeMillis = intent.getStringExtra("trackTimeMillis")
            val artistName = intent.getStringExtra("artistName")
            val primaryGenreName = intent.getStringExtra("primaryGenreName")
            val country = intent.getStringExtra("country")
            val relieseDate = intent.getStringExtra("relieseDate")
            artworkUrl100 = intent.getStringExtra("artworkUrl100").toString()

            if (intent.getStringExtra("collectionName")
                    ?.isNullOrEmpty() == true || intent.getStringExtra("collectionName")
                    ?.contains("No Album") == true // Если нет альбома или ответ сервера содержит No Album то убираем поле с альбомом
            ) {
                binding.tvAlbum.makeGone()// убираем поле альбом если нет альбома
                binding.tvAlbumLeft.makeGone()// убираем поле альбом если нет альбома

            } else {
                collectionName = intent.getStringExtra("collectionName")
                    .toString()// убираем поле альбом если нет альбома
                binding.tvAlbum.makeVisible()// убираем поле альбом если нет альбома
                binding.tvAlbum.text = collectionName// убираем поле альбом если нет альбома
            }
            binding.tvGenre.text = primaryGenreName
            binding.tvCountry.text = country
            binding.tvAlbum.text = collectionName
            binding.tvTime.text = trackTimeMillis
            binding.tvYear.text = formattedYear(relieseDate.toString())

            binding.tvGroup.text = artistName
            binding.tvTrackName.text = trackName


            val options = RequestOptions().centerCrop()//опции для Glide



            Glide.with(binding.imMine.context)
                .load(artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
                .apply(options)
                .placeholder(R.drawable.ph_media_312)
                .error(R.drawable.ph_media_312)
                .into(binding.imMine)

            preparePlayer() // подготовили плеер

        }

        else if (!myTracks.isNullOrEmpty()) {
            val track = myTracks[0] // если myTracks не пуст, возьмем свежий трек для плеера
            loadLastLikedTrack(track)
        }

        binding.play.setOnClickListener {
            playBackControl()
            isPlaying = true
            startUpdateProgress() /*переменную при нажатии на плей перевели в тру и начали обновлять
            прогресс. Так же и с кнопкой пауза, ниже */
        }
        binding.pause.setOnClickListener {
            playBackControl()
            isPlaying = false
            stopUpdateProgress()
        }



    }

    override fun loadLastLikedTrack(track: Track) {
        val trackName = track.trackName// убираем поле альбом если нет альбома
        if (track.collectionName?.isNullOrEmpty() == true || track.collectionName?.contains("No Album") == true) {
            binding.tvAlbum.makeGone()// убираем поле альбом если нет альбома
            binding.tvAlbumLeft.makeGone()// убираем поле альбом если нет альбома

        } else {
            collectionName = track.collectionName.toString() // убираем поле альбом если нет альбома
            binding.tvAlbum.makeVisible()// убираем поле альбом если нет альбома
            binding.tvAlbum.text = collectionName// убираем поле альбом если нет альбома
        }

        val trackTimeMillis = track.trackTimeMillis
        val artistName = track.artistName
        val primaryGenreName = track.primaryGenreName
        val country = track.country
        val relieseDate = track.releaseDate
        artworkUrl100 = track.artworkUrl100
        binding.tvGenre.text = primaryGenreName
        binding.tvCountry.text = country
        binding.tvTime.text = trackTimeMillis
        previewUrl = track.previewUrl
        binding.tvGroup.text = artistName
        binding.tvTrackName.text = trackName
        binding.tvYear.text = formattedYear(relieseDate.toString())
        preparePlayer()

        val options = RequestOptions().centerCrop()//опции для Glide
        val radiusInDP = 2f //опции для Glide
        val densityMultiplier = TypedValue.applyDimension( //опции для Glide
            TypedValue.COMPLEX_UNIT_DIP, //опции для Glide
            1f,
            binding.imMine.context.resources.displayMetrics  //опции для Glide
        )
        val radiusInPX = radiusInDP * densityMultiplier //опции для Glide


        Glide.with(binding.imMine.context)
            .load(artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg"))
            .transform(RoundedCorners(radiusInPX.toInt()))
            .apply(options)
            .placeholder(R.drawable.ph_media_312)
            .error(R.drawable.ph_media_312)
            .into(binding.imMine)

    }

    override fun stopUpdateProgress() {
        handler.removeCallbacksAndMessages(null) // функция отмены колбеков от хендлер
    }

    override fun startUpdateProgress() {
        if(!isPlaying) return
        updateProgress()
        handler.postDelayed({startUpdateProgress()}, 300) // вызывается каждые 300 мс
    }

    override fun updateProgress() {

        val formattedTime =  SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
        binding.progressTime.text = formattedTime // обновили время воспроизведения

    }

    override fun showAudioPlayerScreen(intent: Intent, context: Context) {

      context.startActivity(intent)
    }


    fun formattedYear(date: String): String {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val localDateTime = LocalDateTime.parse(date, formatter)
        val year = localDateTime.year
        return year.toString()
    }
}