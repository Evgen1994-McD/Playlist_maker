package com.example.playlistmaker

import android.content.Context
import android.content.Intent
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
        val backClicker = findViewById<androidx.appcompat.widget.Toolbar>(R.id.settings_toolbar_day)
        backClicker.setNavigationOnClickListener {
            finish()
        }
        val shareTheAppClicker =
            findViewById<com.google.android.material.textview.MaterialTextView>(R.id.shareApp)
        shareTheAppClicker.setOnClickListener {
            shareApp(this@SettingsActivity)
        }

        val mesToSuppClicker =
            findViewById<com.google.android.material.textview.MaterialTextView>(R.id.sMesToSuport)
        mesToSuppClicker.setOnClickListener {
          sendSuppEmail()  //Здесь будет вызван метод
        }
    }

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


    private fun sendSuppEmail(){
        val myEmail = "mrmarwell@gmail.com"
        val subject = "Сообщение разработчиками и разработчицам приложением Playlist Maker"
        val body = "Спасибо разработчикам и разработчицам за крутое приложение!"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"

            putExtra(Intent.EXTRA_EMAIL, arrayOf(myEmail))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        if (intent.resolveActivity(packageManager)!= null){
            startActivity(intent)
        }
        else{
            Toast.makeText(this, "Приложения для отправки отстуствуют!!!", Toast.LENGTH_SHORT).show()
        }
    }

    }
