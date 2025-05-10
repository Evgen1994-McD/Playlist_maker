package com.example.playlistmaker.data.repositories

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.R
import com.example.playlistmaker.data.Constants
import com.example.playlistmaker.data.dto.App
import com.example.playlistmaker.domain.api.SettingsRepository
import com.example.playlistmaker.ui.activity.SettingsActivity
import com.google.android.material.materialswitch.MaterialSwitch

class SettingsReposytoryImpl() : SettingsRepository {
    override fun shareApp(context: Context) : Intent {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                context.getString(R.string.Url_androidDeveloper)
            ) // Ссылка на курс андроид разработки
            type = "text/plain"
        }

        return sendIntent
//        context.startActivity(
//            Intent.createChooser(sendIntent, context.getString(R.string.share_stroke))
      //  )
    }

    override fun sendSuppEmail(myEmail: String, subject : String, body : String) : Intent {
        val intent = Intent(Intent.ACTION_SEND).apply {
            // Указание категории электронной почты
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(myEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        return intent


    }

    override fun openUrlInDefaultBrowser(url: String) : Intent {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addCategory(Intent.CATEGORY_BROWSABLE)
        }
        return intent
    }

    override fun controlAppThemeMode(applicationContext: Context, context: Context, sharedPrefs : SharedPreferences) : Boolean {
        val currentTheme = applicationContext as App

        if(currentTheme.hasBooleanValue(context, Constants.KEY_THEME_MODE)) {
            var savedTheme = sharedPrefs.getBoolean(Constants.KEY_THEME_MODE, false)
            currentTheme.switchTheme(savedTheme)
            return savedTheme
        }else {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
              return currentTheme.isSystemInDarkTheme(context)

        }

    }

}