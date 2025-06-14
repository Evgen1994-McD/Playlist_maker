package com.example.playlistmaker.ui.settings.activity


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.SettingsBinding
import com.example.playlistmaker.ui.settings.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue


class SettingsActivity : AppCompatActivity() {
    private var isUpdatingUI = false

    private lateinit var binding: SettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = SettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val backClicker =
       binding.settingsToolbarDay
        backClicker.setNavigationOnClickListener {
            finish()
        }
        val shareTheAppClicker = // Кликер поделиться приложением
          binding.shareApp
        shareTheAppClicker.setOnClickListener {
            shareApp()
        }
        val mesToSuppClicker = // Пишем в поддержку
            binding.sMesToSuport
        mesToSuppClicker.setOnClickListener {
            sendSuppEmail()  //Здесь будет вызван метод
        }
        val userAssetClicker =
            binding.userAssetUri
        userAssetClicker.setOnClickListener {
           openUrlInDefaultBrowser()
        }
        val switcherTheme = binding.switchTheme



// Подписка на получение изменений из LiveData
        viewModel.getLiveData.observe(this) { currentTheme ->
            if (currentTheme != switcherTheme.isChecked && !isUpdatingUI) {
                isUpdatingUI = true // Блокируем UI-обновления на время операции
                switcherTheme.isChecked = currentTheme
                isUpdatingUI = false // Разрешаем последующие обновления
            }
        }

// Установка обработчика изменений
        switcherTheme.setOnCheckedChangeListener { _, isChecked ->
            if (!isUpdatingUI) {
                viewModel.controlTHemeBySwitcher(isChecked)
            }
        }


    }

    fun shareApp() {  // Метод - интент для отправки сообщений
        viewModel.shareApp()

    }
    private fun sendSuppEmail() {  // Приватный метод для письма в поддержку
        viewModel.sendSuppEmail()
    }

    private fun openUrlInDefaultBrowser() {
        viewModel.openUrlInDefaultBrowser()
    }
    override fun onDestroy() { // закрываем плеер при завершении работы
        super.onDestroy()
        viewModel.getLiveData.removeObservers(this)

    }

}