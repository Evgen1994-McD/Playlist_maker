package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.MainActivity
import com.example.playlistmaker.SearchActivity
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.retrofit.Track
import com.example.playlistmaker.utils.Constants
import okio.IOException
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.toString

class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding // делаю байдинг
    private lateinit var artworkUrl100: String
    private lateinit var storage: TrackStorage


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


        binding.toolbar.setNavigationOnClickListener {  //назад в Майнактивити
            finish()
        }

        storage = TrackStorage(this@MediaActivity) // инициализируем экземпляр класса Trackstorage
        val myTracks = storage.getAllTracks() //все треки


        val sharedPrefs =
            getSharedPreferences(Constants.SHARED_PREF_THEME_NAME, Context.MODE_PRIVATE)
        val theme = applicationContext as App  // загрузка сохранённой темы в SharedPreferences
        if (theme.hasBooleanValue(this@MediaActivity, Constants.KEY_THEME_MODE)) {
            var savedTheme = sharedPrefs.getBoolean(Constants.KEY_THEME_MODE, false)
            theme.switchTheme(savedTheme)
        } else {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM // Выше идёт определение темы приложения
        }

        if (!intent.getStringExtra("trackName")
                .isNullOrEmpty()
        ) // запускаем интент только если интент есть
        {
            intentGetExtraBind() // запустим интент

        } else if (!myTracks.isNullOrEmpty()) {
            val track = myTracks[0] // если myTracks не пуст, возьмем свежий трек для плеера
            loadLastLikedTrack(track)
        }


    }


    fun formatMillisecondsAsMinSec(milliseconds: Long): String { // функция перевода времени
        val localTime = LocalTime.ofNanoOfDay(milliseconds * 1_000_000)
        val formatter = DateTimeFormatter.ofPattern("mm:ss")
        return localTime.format(formatter)
    }

    fun getCoverArtwork(artworkUrl100: String) =
        artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")

    fun intentGetExtraBind() {
        val intent = intent // получаем интент который запустил активность
        val trackName = intent.getStringExtra("trackName")
        val collectionName = intent.getStringExtra("collectionName")
        val trackTimeMillis = intent.getStringExtra("trackTimeMillis")
        val artistName = intent.getStringExtra("artistName")
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")
        val relieseDate = intent.getStringExtra("relieseDate")
        artworkUrl100 = intent.getStringExtra("artworkUrl100").toString()
        binding.tvGenre.text = primaryGenreName
        binding.tvCountry.text = country
        binding.tvAlbum.text = collectionName
        binding.tvTime.text = formatMillisecondsAsMinSec(trackTimeMillis!!.toLong())
        binding.tvYear.text = formattedYear(relieseDate.toString())

        binding.tvGroup.text = artistName
        binding.tvTrackName.text = trackName


        val options = RequestOptions().centerCrop()//опции для Glide
        val radiusInDP = 2f //опции для Glide
        val densityMultiplier = TypedValue.applyDimension( //опции для Glide
            TypedValue.COMPLEX_UNIT_DIP, //опции для Glide
            1f,
            binding.imMine.context.resources.displayMetrics  //опции для Glide
        )
        val radiusInPX = radiusInDP * densityMultiplier //опции для Glide


        Glide.with(binding.imMine.context)
            .load(getCoverArtwork(artworkUrl100.toString()))
            .transform(RoundedCorners(radiusInPX.toInt()))
            .apply(options)
            .placeholder(R.drawable.ph_media_312)
            .error(R.drawable.ph_media_312)
            .into(binding.imMine)

    }

    fun loadLastLikedTrack(track: Track) {
        val trackName = track.trackName
        val collectionName = track.collectionName
        val trackTimeMillis = track.trackTimeMillis
        val artistName = track.artistName
        val primaryGenreName = track.primaryGenreName
        val country = track.country
        val relieseDate = track.releaseDate
        artworkUrl100 = track.artworkUrl100
        binding.tvGenre.text = primaryGenreName
        binding.tvCountry.text = country
        binding.tvAlbum.text = collectionName
        binding.tvTime.text = formatMillisecondsAsMinSec(trackTimeMillis!!.toLong())

        binding.tvGroup.text = artistName
        binding.tvTrackName.text = trackName
        binding.tvYear.text = formattedYear(relieseDate.toString())


        val options = RequestOptions().centerCrop()//опции для Glide
        val radiusInDP = 2f //опции для Glide
        val densityMultiplier = TypedValue.applyDimension( //опции для Glide
            TypedValue.COMPLEX_UNIT_DIP, //опции для Glide
            1f,
            binding.imMine.context.resources.displayMetrics  //опции для Glide
        )
        val radiusInPX = radiusInDP * densityMultiplier //опции для Glide


        Glide.with(binding.imMine.context)
            .load(getCoverArtwork(artworkUrl100.toString()))
            .transform(RoundedCorners(radiusInPX.toInt()))
            .apply(options)
            .placeholder(R.drawable.ph_media_312)
            .error(R.drawable.ph_media_312)
            .into(binding.imMine)


    }

    override fun onSaveInstanceState(outState: Bundle) { // Сохраняем факт видимости аудиоплеера
        super.onSaveInstanceState(outState)// Сохраняем факт видимости аудиоплеера
        outState.putBoolean("isAudioPlayerVisible", true) // Сохраняем факт видимости аудиоплеера
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) { // Восстановим активность
        super.onRestoreInstanceState(savedInstanceState) // Восстановим активность
        if (savedInstanceState.containsKey("isAudioPlayerVisible") && savedInstanceState.getBoolean("isAudioPlayerVisible")) {
            showAudioPlayerScreen()// Отображаем экран аудиоплеера

        }
    }
    fun showAudioPlayerScreen() { // Восстановим активность
        val intent = Intent(this, MediaActivity::class.java) // Восстановим активность
        startActivity(intent) // Восстановим активность
    }

    fun formattedYear(date : String) : String {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val localDateTime = LocalDateTime.parse(date, formatter)
        val year = localDateTime.year
        return year.toString()
    }
}