package com.example.playlistmaker.presentation.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SettingsBinding
import com.example.playlistmaker.presentation.viewModels.SettingsViewModel
import com.example.playlistmaker.presentation.viewModels.ThemeViewModel

class SettingsActivity : AppCompatActivity() {
    private lateinit var themeViewModel: ThemeViewModel
    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: SettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = SettingsBinding.inflate(layoutInflater)
//        setContentView(R.layout.settings)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val factory = SettingsViewModel.CustomViewModelSettingsFactory(
            Creator.provideShareAppUseCase(),
            Creator.provideSendSuppEmailUseCase(),
            Creator.provideOpenUrlUseCase()
        )
        viewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]

        val backClicker =
       binding.settingsToolbarDay
        backClicker.setNavigationOnClickListener {
            finish()
        }
        val shareTheAppClicker = // Кликер поделиться приложением
          binding.shareApp
        shareTheAppClicker.setOnClickListener {
            shareApp(this@SettingsActivity)
        }
        val mesToSuppClicker = // Пишем в поддержку
            binding.sMesToSuport
        mesToSuppClicker.setOnClickListener {
            sendSuppEmail(this@SettingsActivity)  //Здесь будет вызван метод
        }
        val userAssetClicker =
            binding.userAssetUri
        userAssetClicker.setOnClickListener {
           openUrlInDefaultBrowser(this)
        }
        val switcherTheme = binding.switchTheme

        themeViewModel = ViewModelProvider(this, ThemeViewModel.getViewModelFactory())[ThemeViewModel::class.java]  // Инициализируем модел
        switcherTheme.setOnCheckedChangeListener { _, isChecked ->
          themeViewModel.controlTHemeBySwitcher(isChecked)
        }
    }
    fun shareApp(context: Context) {  // Метод - интент для отправки сообщений
        viewModel.shareApp(context)

    }
    private fun sendSuppEmail(context: Context) {  // Приватный метод для письма в поддержку
        viewModel.sendSuppEmail(context)
    }

    private fun openUrlInDefaultBrowser(context: Context) {
        viewModel.openUrlInDefaultBrowser(context)
    }
    override fun onDestroy() { // закрываем плеер при завершении работы
        super.onDestroy()
        themeViewModel.loadingLiveData().removeObservers(this) //удалили обсерверы

    }

}