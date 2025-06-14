package com.example.playlistmaker.ui.main.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.main.viewModel.MainViewModel
import com.example.playlistmaker.ui.player.activity.MediaActivity
import com.example.playlistmaker.ui.search.activity.SearchActivity
import com.example.playlistmaker.ui.settings.activity.SettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val viewModel by viewModel<MainViewModel>()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets }






        viewModel.controlThemeInOtherWindows()


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



            }
            val settingsClicker = binding.settingsDay
            settingsClicker.setOnClickListener {
                val displayIntent = Intent(this, SettingsActivity::class.java)
                startActivity(displayIntent)
            }



    }

    override fun onDestroy() {
        super.onDestroy()

    }

}