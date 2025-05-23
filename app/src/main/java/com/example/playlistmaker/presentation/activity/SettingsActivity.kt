package com.example.playlistmaker.presentation.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.Creator
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.OpenUrlUseCase
import com.example.playlistmaker.domain.api.SendSuppEmailUseCase
import com.example.playlistmaker.domain.api.ShareAppUseCase
import com.example.playlistmaker.domain.api.SwitchThemeUseCase
import com.example.playlistmaker.presentation.viewModels.ThemeViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {
private val shareAppUseCase by lazy { Creator.provideShareAppUseCase()  }
private val sendSuppEmailUseCase by lazy {  Creator.provideSendSuppEmailUseCase() }
private  val opernUrlUseCase by lazy {  Creator.provideOpenUrlUseCase()}
private  val switchThemeUseCase by lazy { Creator.provideSwitchThemeUseCase() }
    private lateinit var themeViewModel: ThemeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        
        val backClicker =
            findViewById<Toolbar>(R.id.settings_toolbar_day) // Назад в MainActivity
        backClicker.setNavigationOnClickListener {
            finish()
        }
        val shareTheAppClicker = // Кликер поделиться приложением
            findViewById<MaterialTextView>(R.id.shareApp)
        shareTheAppClicker.setOnClickListener {
            shareApp(this@SettingsActivity)
        }
        val mesToSuppClicker = // Пишем в поддержку
            findViewById<MaterialTextView>(R.id.sMesToSuport)
        mesToSuppClicker.setOnClickListener {
            sendSuppEmail(this@SettingsActivity)  //Здесь будет вызван метод
        }
        val userAssetClicker =
            findViewById<MaterialTextView>(R.id.userAssetUri)
        userAssetClicker.setOnClickListener {
            // Ссылка, которую нужно открыть
            val url = getString(R.string.Url_userasset)
            openUrlInDefaultBrowser(url, this@SettingsActivity)
        }

        // Найти SwitchMaterial по id
        val switcherTheme = findViewById<SwitchMaterial>(R.id.switchTheme)



//        switchThemeUseCase.switchThemeModeBySettings(
//            switcherTheme
//
//        )

        themeViewModel = ViewModelProvider(this, ThemeViewModel.getViewModelFactory())[ThemeViewModel::class.java]  // Инициализируем модел
        themeViewModel.loadingLiveData().observe(this) { newTheme ->
            switchThemeUseCase.switchThemeModeBySettings(switcherTheme)
        }



    }

    fun shareApp(context: Context) {  // Метод - интент для отправки сообщений
        shareAppUseCase.shareApp(context)

    }


    private fun sendSuppEmail(context: Context) {  // Приватный метод для письма в поддержку
        val myEmail = getString(R.string.address)
        val subject = getString(R.string.subject)
        val body = getString(R.string.body)

        sendSuppEmailUseCase.sendSuppEmail(context, myEmail, subject, body)
    }

    private fun openUrlInDefaultBrowser(url: String, context: Context) {
        opernUrlUseCase.openUrlInDefaultBrowser(context, url)
    }

    override fun onDestroy() { // закрываем плеер при завершении работы
        super.onDestroy()


        themeViewModel.loadingLiveData().removeObservers(this) //удалили обсерверы

    }

}