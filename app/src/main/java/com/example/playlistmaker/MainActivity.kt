package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets


        }
        val searchClicker = findViewById<Button>(R.id.search_day)
        searchClicker.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val context = v?.context ?: return // Получаем контекст из представления
                val displayIntentSrc = Intent(context, SearchActivity::class.java)
                startActivity(displayIntentSrc)
            }
        })


        val mediaClicker = findViewById<Button>(R.id.media_day)
        mediaClicker.setOnClickListener{
            val displayIntentMedia = Intent(this,MediaActivity::class.java)
            startActivity(displayIntentMedia)

        }
        val settingsClicker = findViewById<Button>(R.id.settings_day)
        settingsClicker.setOnClickListener{
           val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }
    }

}