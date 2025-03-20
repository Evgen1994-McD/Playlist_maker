package com.example.playlistmaker

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.utils.Constants
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
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
            findViewById<androidx.appcompat.widget.Toolbar>(R.id.settings_toolbar_day) // Назад в MainActivity
        backClicker.setNavigationOnClickListener {
            finish()
        }
        val shareTheAppClicker = // Кликер поделиться приложением
            findViewById<com.google.android.material.textview.MaterialTextView>(R.id.shareApp)
        shareTheAppClicker.setOnClickListener {
            shareApp(this@SettingsActivity)
        }
        val mesToSuppClicker = // Пишем в поддержку
            findViewById<com.google.android.material.textview.MaterialTextView>(R.id.sMesToSuport)
        mesToSuppClicker.setOnClickListener {
            sendSuppEmail()  //Здесь будет вызван метод
        }
        val userAssetClicker =
            findViewById<com.google.android.material.textview.MaterialTextView>(R.id.userAssetUri)
        userAssetClicker.setOnClickListener {
            // Ссылка, которую нужно открыть
            val url = getString(R.string.Url_userasset)
            openUrlInDefaultBrowser(url)
        }

        // Найти SwitchMaterial по id
        val switcherTheme = findViewById<SwitchMaterial>(R.id.switchTheme)

        val sharedPrefs =
            getSharedPreferences(Constants.SHARED_PREF_THEME_NAME, Context.MODE_PRIVATE)

        val theme = applicationContext as App  // загрузка сохранённой темы в SharedPreferences
        if(theme.hasBooleanValue(this@SettingsActivity, Constants.KEY_THEME_MODE)) {
            var savedTheme = sharedPrefs.getBoolean(Constants.KEY_THEME_MODE, false)
            theme.switchTheme(savedTheme)
            switcherTheme.isChecked = savedTheme
        }else {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            switcherTheme.isChecked = theme.isSystemInDarkTheme(this)
        }

        switcherTheme.setOnCheckedChangeListener { _, isChecked ->
            // Сохранить новое значение темы in Sharedpreferences
            sharedPrefs.edit().putBoolean(Constants.KEY_THEME_MODE, isChecked).apply()

            // Переключить тему
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            // Перезапустить активность для применения новой темы
            recreate()
        }


    }

    fun shareApp(context: Context) {  // Метод - интент для отправки сообщений
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                context.getString(R.string.Url_androidDeveloper)
            ) // Ссылка на курс андроид разработки
            type = "text/plain"
        }
        context.startActivity(
            Intent.createChooser(sendIntent, context.getString(R.string.share_stroke))
        )
    }

    private fun sendSuppEmail() {  // Приватный метод для письма в поддержку
        val myEmail = getString(R.string.address)
        val subject = getString(R.string.subject)
        val body = getString(R.string.body)

        val intent = Intent(Intent.ACTION_SEND).apply {
            // Указание категории электронной почты
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(myEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, getString(R.string.noApp_stroke), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun openUrlInDefaultBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addCategory(Intent.CATEGORY_BROWSABLE)
        }
        startActivity(intent)
    }

}