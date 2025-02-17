package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class SearchActivity : AppCompatActivity() {
    @SuppressLint("ClickableViewAccessibility")
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

        clearEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.isNullOrBlank()){
                    clearEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        ContextCompat.getDrawable(this@SearchActivity, R.drawable.searchforhint_icon),
                        null,
                        ContextCompat.getDrawable(this@SearchActivity, R.drawable.clearsearch),
                        null
                    )

                }
                else{
                    clearEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        ContextCompat.getDrawable(this@SearchActivity, R.drawable.searchforhint_icon),
                        null,
                        null,
                        null
                    )
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                //empty
            }
        }
        )
        clearEditText.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEndBounds = clearEditText.compoundDrawables[2]?.bounds
                if(drawableEndBounds!= null){
                val x = event.x.toInt()
                val y = event.y.toInt()
                if (x >= (view.width - (drawableEndBounds.width()+view.paddingRight)) &&
                    x <= view.width-view.paddingRight && y >= 0 && y <= view.height
                ) {
                    clearEditText.text?.clear()   // чистим эдит текст
                    return@setOnTouchListener true
                }
            }
            }
            false
        }



    }
}