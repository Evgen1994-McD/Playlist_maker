package com.example.playlistmaker.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.Creator
import com.example.playlistmaker.data.dto.App
import com.example.playlistmaker.R
import com.example.playlistmaker.data.Constants
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textview.MaterialTextView

class SettingsActivity : AppCompatActivity() {

    val shareAppInteractor = Creator.provideShareAppInteractor() // интерактор Поделиться приложением
val sendSuppEmailInteractor = Creator.provideSendSuppEmailInteracto()
    val opernUrlInteractor = Creator.provideOpenUrlInteractor()
val switchThemeInteractor = Creator.provideSwitchThemeInteractor()
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

        val sharedPrefs =
            getSharedPreferences(Constants.SHARED_PREF_THEME_NAME, MODE_PRIVATE)


        switchThemeInteractor.switchThemeModeBySettings(switcherTheme,
            applicationContext as App,
            this@SettingsActivity,
            sharedPrefs,
            this@SettingsActivity)



    }

    fun shareApp(context: Context) {  // Метод - интент для отправки сообщений
        shareAppInteractor.shareApp(context)

    }


    private fun sendSuppEmail(context: Context) {  // Приватный метод для письма в поддержку
        val myEmail = getString(R.string.address)
        val subject = getString(R.string.subject)
        val body = getString(R.string.body)

        sendSuppEmailInteractor.sendSuppEmail(context, myEmail, subject, body)
    }

    private fun openUrlInDefaultBrowser(url: String, context: Context) {
       opernUrlInteractor.openUrlInDefaultBrowser(context,url)
    }

}