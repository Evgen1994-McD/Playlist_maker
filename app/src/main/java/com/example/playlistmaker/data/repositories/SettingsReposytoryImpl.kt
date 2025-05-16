package com.example.playlistmaker.data.repositories

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.R
import com.example.playlistmaker.data.Constants
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.api.SettingsRepository

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

    override fun controlAppThemeMode(applicationContext: Context): Boolean {
        val themeSharedPrefs =
            context.getSharedPreferences(Constants.SHARED_PREF_THEME_NAME, Context.MODE_PRIVATE)

        val savedTheme = themeSharedPrefs.getBoolean(Constants.KEY_THEME_MODE, false)
        if (savedTheme != null) {
       switchTheme(savedTheme)
            return savedTheme
        } else {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            val currentNightMode =
                context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return currentNightMode == Configuration.UI_MODE_NIGHT_YES
        }


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