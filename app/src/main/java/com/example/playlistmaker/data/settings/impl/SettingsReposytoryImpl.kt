package com.example.playlistmaker.data.settings.impl

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.R
import com.example.playlistmaker.utils.Constants
import com.example.playlistmaker.domain.settings.SettingsRepository

class SettingsReposytoryImpl(private val context: Context) : SettingsRepository {
    override fun shareApp(): Intent {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                context.getString(R.string.Url_androidDeveloper)
            ) // Ссылка на курс андроид разработки
            type = "text/plain"
        }

        return sendIntent

//            Intent.createChooser(sendIntent, context.getString(R.string.share_stroke))
        //  )
    }

    override fun sendSuppEmail(myEmail: String, subject: String, body: String): Intent {
        val intent = Intent(Intent.ACTION_SEND).apply {
            // Указание категории электронной почты
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(myEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        return intent


    }

    override fun openUrlInDefaultBrowser(url: String): Intent {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addCategory(Intent.CATEGORY_BROWSABLE)
        }
        return intent
    }




    override fun controlAppThemeMode(): Boolean {
        val themeSharedPrefs = context.getSharedPreferences(Constants.SHARED_PREF_THEME_NAME, Context.MODE_PRIVATE)

        // Проверяем наличие ключа перед чтением значения
        if (!themeSharedPrefs.contains(Constants.KEY_THEME_MODE)) {
            // Ключ отсутствует, используем режим системы
            val currentNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            when (currentNightMode) {
                Configuration.UI_MODE_NIGHT_YES -> {
                    switchTheme(true) // Включаем тёмную тему
                    return true
                }
                Configuration.UI_MODE_NIGHT_NO -> {
                    switchTheme(false) // Оставляем светлую тему
                    return false
                }
            }
        } else {
            // Ключ присутствует, получаем и применяем сохранённую тему
            val savedTheme = themeSharedPrefs.getBoolean(Constants.KEY_THEME_MODE, false)
            switchTheme(savedTheme)
            return savedTheme
        }
        return false
    }

    override fun saveCurrentThemeToShared(isChecked: Boolean) {
        val themeSharedPrefs =
            context.getSharedPreferences(Constants.SHARED_PREF_THEME_NAME, Context.MODE_PRIVATE)
        themeSharedPrefs.run {
            edit().putBoolean(Constants.KEY_THEME_MODE, isChecked).apply()
        }

    }


    override fun switchTheme(savedTheme: Boolean) {

        if (savedTheme != null) {
            AppCompatDelegate.setDefaultNightMode(
                if (savedTheme) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }

    }

}