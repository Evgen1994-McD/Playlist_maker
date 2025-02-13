package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.searchLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val backClicker = findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_toolbar) // Назад в MainActivity
        backClicker.setNavigationOnClickListener {
            finish()
        }

        val clearEditText = findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.search_stroke)
        clearEditText.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEndBounds = clearEditText.compoundDrawables[2].bounds
                val x = event.x.toInt()
                val y = event.y.toInt()
                if (x >= (view.width - drawableEndBounds.width()) &&
                    x <= view.width && y >= 0 && y <= view.height
                ) {
                    Toast.makeText(this@SearchActivity, "Иконка нажата!", Toast.LENGTH_SHORT).show()
                    return@setOnTouchListener true
                }
            }
            false
        }
    }
}