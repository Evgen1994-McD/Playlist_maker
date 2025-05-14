package com.example.playlistmaker.presentation.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.Creator
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.domain.api.FavoriteTrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.api.MediaInteractor

class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding // делаю байдинг
    private lateinit var artworkUrl100: String
    private lateinit var collectionName: String
    private lateinit var previewUrl: String
    private var isPlaying = false // переменная статуса плеера
private lateinit var favoriteTrackInteractorImpl : FavoriteTrackInteractor
    private lateinit var mediaPlayerInteractor: MediaInteractor

    companion object { // компаньон медиаплеера

        private const val default_time = "00:00"
    }

    private val handler = Handler(Looper.getMainLooper()) // хэндлер для доступа к главному потоку


    private fun onPlayerReady() { // это функция для листенера
        binding.play.isEnabled = true
        startUpdateProgress()
    }

    private fun onPlayComplete() { // это тоже
        stopUpdateProgress()
        binding.pause.makeInvisible()
        binding.play.makeVisible()
        binding.progressTime.text = default_time
        Log.d("MediaPlayer", "Проигрывание завершено")

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_media)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mediaPlayerInteractor = Creator.provideMediaInteractor() //создали интерактор

        collectionName = "" // инициализировал


        binding.toolbar.setNavigationOnClickListener {  //назад в Майнактивити
            finish()
        }

        favoriteTrackInteractorImpl =
            Creator.provideFavoriteInteractor(this) // создал экземлпр фаворитинтерактора для доступа к коллекции

        val myTracks =
            favoriteTrackInteractorImpl.getAllTracksFromStorage()//storage.getAllTracks() //все треки

        mediaPlayerInteractor.addListeners(
            ::onPlayerReady, ::onPlayComplete
        )  // листенер для определения начала и окончания воспроизведения


        val switchThemeUseCase = Creator.provideSwitchThemeUseCase()
        switchThemeUseCase.controlThemeInOtherWindows(
            applicationContext as App, this@MediaActivity
        )

        if (!intent.getStringExtra("trackName")
                .isNullOrEmpty()
        ) // запускаем интент только если интент есть
        {
            intentGetExtraBind() // запустим интент
        } else if (!myTracks.isNullOrEmpty()) {
            val track = myTracks[0] // если myTracks не пуст, возьмем свежий трек для плеера
            loadLastLikedTrack(track)
        }

        binding.play.setOnClickListener {

                binding.play.isEnabled = true
                binding.play.makeInvisible()
                binding.pause.makeVisible()
                mediaPlayerInteractor.startPlayback()
                isPlaying = true
                startUpdateProgress()

        }
        binding.pause.setOnClickListener {

                binding.pause.makeInvisible()


                binding.play.makeVisible()
                mediaPlayerInteractor.pausePlayback()
                isPlaying = false
                stopUpdateProgress()

        }
    }


    fun intentGetExtraBind() {
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
        binding.tvYear.text = relieseDate

        binding.tvGroup.text = artistName
        binding.tvTrackName.text = trackName


        val options = RequestOptions().centerCrop()//опции для Glide

        Glide.with(binding.imMine.context).load(artworkUrl100).apply(options)
            .placeholder(R.drawable.ph_media_312).error(R.drawable.ph_media_312)
            .into(binding.imMine)
        Log.d("MediaActivity", "Preview URL: $previewUrl")
        mediaPlayerInteractor.preparePlayer(previewUrl)

    }


    fun loadLastLikedTrack(track: Track) {// убираем поле альбом если нет альбома
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
        binding.tvYear.text = relieseDate
        mediaPlayerInteractor.preparePlayer(previewUrl)
        val options = RequestOptions().centerCrop()//опции для Glide
        val radiusInDP = 2f //опции для Glide
        val densityMultiplier = TypedValue.applyDimension( //опции для Glide
            TypedValue.COMPLEX_UNIT_DIP, //опции для Glide
            1f, binding.imMine.context.resources.displayMetrics  //опции для Glide
        )
        val radiusInPX = radiusInDP * densityMultiplier //опции для Glide


        Glide.with(binding.imMine.context).load(artworkUrl100)
            .transform(RoundedCorners(radiusInPX.toInt())).apply(options)
            .placeholder(R.drawable.ph_media_312).error(R.drawable.ph_media_312)
            .into(binding.imMine)


    }

    override fun onSaveInstanceState(outState: Bundle) { // Сохраняем факт видимости аудиоплеера
        super.onSaveInstanceState(outState)// Сохраняем факт видимости аудиоплеера
        outState.putBoolean("isAudioPlayerVisible", true) // Сохраняем факт видимости аудиоплеера
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) { // Восстановим активность
        super.onRestoreInstanceState(savedInstanceState) // Восстановим активность
        if (savedInstanceState.containsKey("isAudioPlayerVisible") && savedInstanceState.getBoolean(
                "isAudioPlayerVisible"
            )
        ) {
            showAudioPlayerScreen()// Отображаем экран аудиоплеера

        }
    }

    fun showAudioPlayerScreen() { // Восстановим активность
        val intent = Intent(this, MediaActivity::class.java) // Восстановим активность
        startActivity(intent) // Восстановим активность
    }


    private fun View.makeGone() {
        this.visibility = View.GONE // функция для вью гон
    }

    private fun View.makeVisible() {
        this.visibility = View.VISIBLE // функция для вью визибл
    }

    private fun View.makeInvisible() {
        this.visibility = View.INVISIBLE // функция для вью инвизибл
    }

    override fun onPause() { //пауза когда сворачиваем
        super.onPause()
        mediaPlayerInteractor.pausePlayback()
    }

    override fun onDestroy() { // закрываем плеер при завершении работы
        super.onDestroy()

        mediaPlayerInteractor.releasePlayer()
        stopUpdateProgress()
    }


    fun stopUpdateProgress() {
        handler.removeCallbacksAndMessages(null) // функция отмены колбеков от хендлер

    }

    @SuppressLint("SuspiciousIndentation")
    fun startUpdateProgress() {
        if (!isPlaying) return
        val progress = mediaPlayerInteractor.updateProgress()
        binding.progressTime.text = progress
        handler.postDelayed({ startUpdateProgress() }, 300) // вызывается каждые 300 мс
    }


}