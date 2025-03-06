package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.retrofit.ITunesApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class SearchActivity : AppCompatActivity() {
    private lateinit var clearEditText: EditText
    private lateinit var txtForSearch: String
    private var textFromInput: String = null.toString()
    private val keyForWatcher: String =
        "keyForWatcherSearch"  // Константа для ватчера
    private lateinit var phForNothingToShow: ImageView
    private lateinit var msgTopTxt: TextView
    private lateinit var msgBotTxt: TextView
    private lateinit var buttonNoInternet : Button
    private val iTunesBaseUrl = "https://itunes.apple.com"

    private lateinit var recyclerView: RecyclerView

    @SuppressLint("ClickableViewAccessibility", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.searchLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        phForNothingToShow =
            findViewById<ImageView>(R.id.ph_ntsh_120)
        clearEditText =
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.search_stroke)

        msgTopTxt =
            findViewById<TextView>(R.id.msg_noint_top_txt)

        msgBotTxt =
            findViewById<TextView>(R.id.msg_noint_bottom_txt)

        buttonNoInternet =
            findViewById<Button>(R.id.button_nointernet)

        recyclerView =
            findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.track_list)
        val interceptor =
            HttpLoggingInterceptor()  // Вылетает при запросах, пробую интерсепотором понять почему
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()


        val retroFit = Retrofit.Builder()  // ретрофит
            .baseUrl(iTunesBaseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val iTunesApi = retroFit.create(ITunesApi::class.java)
buttonNoInternet.setOnClickListener { // Кнопка поиска при отсутствии интернета
    phForNothingToShow.visibility = View.GONE
    recyclerView.visibility = View.GONE
    msgTopTxt.visibility = View.GONE
    msgBotTxt.visibility = View.GONE
    buttonNoInternet.visibility = View.GONE
    txtForSearch = clearEditText.text.toString() // текст для поиска
    searchSongs(txtForSearch, iTunesApi) // передаю параметры для поиска в метод. Пробую передать ту же логику, что и в поиске, ведь кнопку будет видно только при определенных условиях

}
        clearEditText.setOnEditorActionListener { _, actionId, _ ->        // слушатель done-enter
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                phForNothingToShow.visibility = View.GONE
                recyclerView.visibility = View.GONE
                msgTopTxt.visibility = View.GONE
                msgBotTxt.visibility = View.GONE
                buttonNoInternet.visibility = View.GONE
                txtForSearch = clearEditText.text.toString() // текст для поиска
                searchSongs(txtForSearch, iTunesApi) // передаю параметры для поиска в метод
                true

            }
            false
        }


        val backClicker =
            findViewById<androidx.appcompat.widget.Toolbar>(R.id.search_toolbar) // Назад в MainActivity
        backClicker.setNavigationOnClickListener {
            finish()
        }

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
        val clearEditText =  // инициализирую эдиттекст
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
                        recyclerView.visibility = View.INVISIBLE  // убрали список треков при очистке эдиттекста
                        msgTopTxt.visibility = View.GONE  //Убрали сообщение топ
                        msgBotTxt.visibility = View.GONE // убрали сообщение бот
                        phForNothingToShow.visibility = View.GONE // убрали плейсхолдер
                        buttonNoInternet.visibility = View.GONE // убрали кнопку

                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }

    private fun searchSongs(txtFromInput: String, iTunesApi: ITunesApi) {
        CoroutineScope(Dispatchers.IO).launch {      // Используем корутины чтобы не грузить поток метод для поиска песен

            try {
                val response = iTunesApi.getSong(txtFromInput)
                val trackResponse = response.body()
                val tracks =
                    trackResponse?.results   // не получалось пока не изменил тип возвращаемого значения в интерфейсе !
                runOnUiThread {  // главный поток
                    if (response.isSuccessful) {
                        if (tracks.isNullOrEmpty()) {
                            val phNts = ContextCompat.getDrawable(
                                this@SearchActivity,
                                R.drawable.ph_nothing_to_show_120
                            )
                            phForNothingToShow.setImageDrawable(phNts)
                            phForNothingToShow.visibility = View.VISIBLE
                            msgTopTxt.visibility = View.VISIBLE
                            msgTopTxt.text = getString(R.string.msg_nothing_to_show)
                        } else {
                            phForNothingToShow.visibility = View.GONE
                            msgBotTxt.visibility = View.GONE
                            msgTopTxt.visibility = View.GONE
                            buttonNoInternet.visibility = View.GONE
                            val recyclerView = findViewById<RecyclerView>(R.id.track_list)
                            recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
                            recyclerView.adapter = TrackAdapter(tracks)
                            recyclerView.visibility = View.VISIBLE

                        }
                    } else {
                        val phNts = ContextCompat.getDrawable(
                            this@SearchActivity,
                            R.drawable.ph_no_internet_120
                        )
                        phForNothingToShow.setImageDrawable(phNts) // To do
                        phForNothingToShow.visibility = View.VISIBLE
                        msgTopTxt.visibility = View.VISIBLE
                        msgTopTxt.text = getString(R.string.msg_no_internet_top)
                        msgBotTxt.visibility = View.VISIBLE
                        msgBotTxt.text = getString(R.string.msg_no_internet_bottom)
                        buttonNoInternet.visibility = View.VISIBLE

                    }
                }
            } catch (e: IOException) {
                runOnUiThread {
                    val phNts = ContextCompat.getDrawable(
                        this@SearchActivity,
                        R.drawable.ph_no_internet_120
                    )
                    phForNothingToShow.setImageDrawable(phNts) // To do
                    phForNothingToShow.visibility = View.VISIBLE
                    msgTopTxt.visibility = View.VISIBLE
                    msgTopTxt.text = getString(R.string.msg_no_internet_top)
                    msgBotTxt.visibility = View.VISIBLE
                    msgBotTxt.text = getString(R.string.msg_no_internet_bottom)
                    buttonNoInternet.visibility = View.VISIBLE

                }


            }

        }
    }

}