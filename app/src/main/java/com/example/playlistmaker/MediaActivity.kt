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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.MainActivity
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.example.playlistmaker.utils.Constants

class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding // делаю байдинг



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

        val options = RequestOptions().centerCrop()//опции для Glide
        val radiusInDP = 2f //опции для Glide
        val densityMultiplier = TypedValue.applyDimension( //опции для Glide
            TypedValue.COMPLEX_UNIT_DIP, //опции для Glide
            1f,
            binding.imMine.context.resources.displayMetrics  //опции для Glide
        )
        val radiusInPX = radiusInDP * densityMultiplier //опции для Glide


        val sharedPrefs =
            getSharedPreferences(Constants.SHARED_PREF_THEME_NAME, Context.MODE_PRIVATE)
        val theme = applicationContext as App  // загрузка сохранённой темы в SharedPreferences
        if(theme.hasBooleanValue(this@MediaActivity, Constants.KEY_THEME_MODE)) {
            var savedTheme = sharedPrefs.getBoolean(Constants.KEY_THEME_MODE, false)
            theme.switchTheme(savedTheme)
        }else {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM // Выше идёт определение темы приложения
        }
val intent = intent // получаем интент который запустил активность
        val trackName = intent.getStringExtra("trackName")
        val collectionName = intent.getStringExtra("collectionName")
        val trackTimeMillis = intent.getStringExtra("trackTimeMillis")
        val artistName = intent.getStringExtra("artistName")
        val primaryGenreName = intent.getStringExtra("primaryGenreName")
        val country = intent.getStringExtra("country")
        val artworkUrl100 = intent.getStringExtra("artworkUrl100")
        binding.tvGenre.text = primaryGenreName
        binding.tvCountry.text = country
        binding.tvAlbum.text=collectionName
        binding.tvTime.text = trackTimeMillis
        binding.tvGroup.text = artistName
        binding.tvTrackName.text = trackName




        Glide.with(binding.imMine.context)
            .load(artworkUrl100)
            .transform(RoundedCorners(radiusInPX.toInt()))
            .apply(options)
            .placeholder(R.drawable.ic_placeholder_45)
            .error(R.drawable.ic_placeholder_45)
            .into(binding.imMine)





    }
}