package com.example.playlistmaker.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.presentation.viewModels.ThemeViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var themeViewModel : ThemeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        themeViewModel = ViewModelProvider(this, ThemeViewModel.getViewModelFactory())[ThemeViewModel::class.java]  // Инициализируем модел
        themeViewModel.controlThemeInOtherWindows()








        val searchClicker = binding.searchDay
        searchClicker.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val context = v?.context ?: return // Получаем контекст из представления
                val displayIntentSrc = Intent(context, SearchActivity::class.java)
                startActivity(displayIntentSrc)

            }
        })
        val mediaClicker = binding.mediaDay
        mediaClicker.setOnClickListener {
            val displayIntentMedia = Intent(this, MediaActivity::class.java)
        startActivity(displayIntentMedia)


        }
        val settingsClicker = binding.settingsDay
        settingsClicker.setOnClickListener {
            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        themeViewModel.loadingLiveData().removeObservers(this) //удалили обсерверы
    }

}