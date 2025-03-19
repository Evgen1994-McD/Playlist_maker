package com.example.playlistmaker

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import android.widget.ImageView
import com.example.playlistmaker.utils.Constants

class MediaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_media)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_media)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val sharedPrefsTheme = getSharedPreferences(Constants.SHARED_PREF_THEME_NAME, Context.MODE_PRIVATE) // загрузка сохранённой темы в SharedPreferences
        var savedTheme = sharedPrefsTheme.getBoolean(Constants.KEY_THEME_MODE, false)
        val theme = applicationContext as App
        theme.switchTheme(savedTheme)

    }
}