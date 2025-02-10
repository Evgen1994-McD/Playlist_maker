package com.example.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

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
            val url = "https://yandex.ru/legal/practicum_offer/"

            // Получаем список всех установленных браузеров
            val browsers = getInstalledBrowsers()

            // Находим браузер по умолчанию
            val defaultBrowser = getDefaultBrowser(browsers)

            // Открываем ссылку в браузере по умолчанию
            openUrlInDefaultBrowser(defaultBrowser, url)
        }


    } // Скоба OnCreate. Ниже нее делаю методы для задания.


    fun shareApp(context: Context) {  // Метод - интент для отправки сообщений
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "https://practicum.yandex.ru/android-developer/?from=catalog"
            ) // Ссылка на курс андроид разработки
            type = "text/plain"
        }
        context.startActivity(
            Intent.createChooser(sendIntent, "Поделитесь нашим приложением")
        )
    }


    private fun sendSuppEmail() {  // Приватный метод для письма в поддержку
        val myEmail = "mrmarwell@gmail.com"
        val subject = "Сообщение разработчиками и разработчицам приложением Playlist Maker"
        val body = "Спасибо разработчикам и разработчицам за крутое приложение!"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"

            putExtra(Intent.EXTRA_EMAIL, arrayOf(myEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Приложения для отправки отстуствуют!!!", Toast.LENGTH_SHORT)
                .show()
        }

    }


    private fun getInstalledBrowsers(): List<String> {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://"))
        val pm = packageManager
        return pm.queryIntentActivities(intent, 0)
            .map { it.activityInfo.packageName }
            .distinct()
    }

    private fun getDefaultBrowser(browserPackages: List<String>): String? {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://"))
        val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return resolveInfo?.activityInfo?.packageName
    }

    private fun openUrlInDefaultBrowser(pkg: String?, url: String) {
        if (pkg != null) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.`package` = pkg
            try {
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Не удалось открыть ссылку в браузере.", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(this, "Браузера по умолчанию не установлено.", Toast.LENGTH_SHORT).show()
        }

    }
}
