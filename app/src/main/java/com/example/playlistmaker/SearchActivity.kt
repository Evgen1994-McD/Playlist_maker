package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    private lateinit var clearEditText: EditText
    private var textFromInput: String = null.toString()
    private val keyForWatcher: String =
        "keyForWatcherSearch"  // Константа для ватчера

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
        val backClicker =
            findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_toolbar) // Назад в MainActivity
        backClicker.setNavigationOnClickListener {
            finish()
        }
        val clearEditText =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.search_stroke)
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager // Для того чтобы спрятать клаву

        savedInstanceState?.let {     // Проверяем, есть ли сохранённый текст в эдит тексте
            val savedText = it.getString(keyForWatcher)
            if (savedText != null) {
                clearEditText.setText(savedText)
            }
        }
        clearEditText.setOnClickListener {
            inputMethodManager.showSoftInput(
                clearEditText,
                0
            )  // Появление клавиатуры при нажатии на эдиттекст
        }

        clearEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //empty
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                logicClearIc(p0)
            }
            // функция логики отображения иконок

            override fun afterTextChanged(p0: Editable?) {
                //empty
            }
        }
        )



        clearTextFromEditText()  //Логика очистки текста

        val recyclerView = findViewById<RecyclerView>(R.id.track_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TrackAdapter(myTrack)



    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(keyForWatcher, textFromInput)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Извлечение данных из Bundle
        val savedText = savedInstanceState.getString(keyForWatcher)
        if (savedText != null) {
            clearEditText.setText(savedText)
        }
    }


    private fun logicClearIc(s: CharSequence?) {
        val clearEditText =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.search_stroke)
        if (!s.isNullOrBlank()) {  // Перенести в функцию
            clearEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                ContextCompat.getDrawable(this@SearchActivity, R.drawable.ic_hintsearch_16),
                null,
                ContextCompat.getDrawable(this@SearchActivity, R.drawable.ic_clear_16),
                null
            )
            textFromInput = s.toString()

        } else {
            clearEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                ContextCompat.getDrawable(this@SearchActivity, R.drawable.ic_hintsearch_16),
                null,
                null,
                null
            )
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun clearTextFromEditText() {
        val clearEditText =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.search_stroke)
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        clearEditText.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEndBounds = clearEditText.compoundDrawables[2]?.bounds
                if (drawableEndBounds != null) {
                    val x = event.x.toInt()
                    val y = event.y.toInt()
                    if (x >= (view.width - (drawableEndBounds.width() + view.paddingRight)) &&
                        x <= view.width - view.paddingRight && y >= 0 && y <= view.height
                    ) {
                        clearEditText.text?.clear()
                        inputMethodManager.hideSoftInputFromWindow(
                            currentFocus?.windowToken,
                            0
                        ) // Прячем клаву
                        // чистим эдит текст
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }
}