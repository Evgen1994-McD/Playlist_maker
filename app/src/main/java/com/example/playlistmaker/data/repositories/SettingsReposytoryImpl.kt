package com.example.playlistmaker.data.repositories

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.SettingsRepository

class SettingsReposytoryImpl() : SettingsRepository {
    override fun shareApp(context: Context) {
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
}