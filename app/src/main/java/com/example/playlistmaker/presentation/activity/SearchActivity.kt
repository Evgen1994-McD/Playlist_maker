package com.example.playlistmaker.presentation.activity

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.OnTrackClickListener
import com.example.playlistmaker.data.repositories.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.App
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.adapters.TrackAdapter
import com.example.playlistmaker.presentation.viewModels.SearchViewModel
import com.example.playlistmaker.presentation.viewModels.ThemeViewModel
import kotlinx.coroutines.Runnable

class SearchActivity : AppCompatActivity(),
    OnTrackClickListener {  // Добавили имлементацию нашего интерфейса OnTrackClickListener для того чтобы определить трек
    private lateinit var searchEditText: AppCompatEditText
    private lateinit var binding: ActivitySearchBinding // делаю байдинг
    private lateinit var txtForSearch: String
    private var textFromInput: String = null.toString()
    private val keyForWatcher: String =
        "keyForWatcherSearch"  // Константа для ватчера
    private lateinit var phForNothingToShow: ImageView
    private lateinit var msgTopTxt: TextView
    private lateinit var msgBotTxt: TextView
    private lateinit var buttonNoInternet: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvMsgSearch: TextView
    private lateinit var btCleanHistory: TextView
    private lateinit var myTracks: List<Track>
    private lateinit var favoriteAdapter: TrackAdapter //адаптер будущий
    private lateinit var pbs: ProgressBar
    private lateinit var viewModel: SearchViewModel
    private lateinit var themeViewModel: ThemeViewModel

    private val handler =
        Handler(Looper.getMainLooper()) // Сделал Хандлер для доступа к главному потоку
   private val trackInteractor by lazy {  Creator.provideTracksInteractor()}
    private val favoriteTrackInteractor by lazy {
    Creator.provideFavoriteInteractor() }// Создал Фаворитинтерактор
   private val switchThemeInteractor by lazy{Creator.provideSwitchThemeUseCase()}

    @SuppressLint("ClickableViewAccessibility", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.searchLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        themeViewModel = ViewModelProvider(this, ThemeViewModel.getViewModelFactory())[ThemeViewModel::class.java]  // Инициализируем модел
        themeViewModel.loadingLiveData().observe(this) { newTheme ->
            switchThemeInteractor.controlThemeInOtherWindows()
        }



       viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory())[SearchViewModel::class.java]

viewModel.getLiveData.observe(this){ newState ->
    when {
        newState.isLoading -> pbs.makeVisible()
            //!newState.isLoading -> pbs.makeInvisible()
        !newState.errorMessage.isNullOrEmpty() && newState.isLoading==false -> {
            handleNoInternetConnection()
            pbs.makeGone()

        }
        newState.searchResults.isNullOrEmpty()  && newState.errorMessage == null -> {
            pbs.makeGone()
            handleNoResults()
        }
        !newState.searchResults.isNullOrEmpty()->{

        displayTracks(newState.searchResults)
        pbs.makeGone()
        }
    }

}

        favoriteAdapter =
            TrackAdapter(favoriteTrackInteractor.getAllTracksFromStorage(), this@SearchActivity)






        pbs = findViewById<ProgressBar>(R.id.pbs)

        tvMsgSearch =
            findViewById<TextView>(R.id.tv_msg_search)

        btCleanHistory =
            findViewById<TextView>(R.id.bt_cleanHistory)




        searchEditText =  // инициализирую эдиттекст
            findViewById<AppCompatEditText>(R.id.search_stroke)

        phForNothingToShow =
            findViewById<ImageView>(R.id.ph_ntsh_120)



        msgTopTxt =
            findViewById<TextView>(R.id.msg_noint_top_txt)

        msgBotTxt =
            findViewById<TextView>(R.id.msg_noint_bottom_txt)

        buttonNoInternet =
            findViewById<TextView>(R.id.button_nointernet)

        recyclerView =
            findViewById<RecyclerView>(R.id.track_list)




        buttonNoInternet.setOnClickListener { // Кнопка поиска при отсутствии интернета
            phForNothingToShow.makeGone()
            recyclerView.makeGone()
            msgTopTxt.makeGone()
            msgBotTxt.makeGone()
            buttonNoInternet.makeGone()
            txtForSearch = searchEditText.text.toString() // текст для поиска
            viewModel.searchTracks(txtForSearch)
//            searchTracks(txtForSearch)

        }

        btCleanHistory.setOnClickListener {  // кнопка очистки истории
            favoriteTrackInteractor.clearHistory()
            recyclerView.makeInvisible() // делаю ресайклер вью невидимым
            tvMsgSearch.makeInvisible() //делаем сообщение "Вы искали" невидимым
            btCleanHistory.makeInvisible() // делаем саму кнопку невидимой при выполнении логики
            searchEditText.clearFocus()  // убираю фокус
        }
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && searchEditText.text?.isNullOrEmpty() == true && !favoriteTrackInteractor.getAllTracksFromStorage()
                    .isNullOrEmpty()
            ) {
                updateTracksFromStorage()
                displayFavoriteTracks()

            }
        }


        val backClicker =
            findViewById<Toolbar>(R.id.search_toolbar) // Назад в MainActivity
        backClicker.setNavigationOnClickListener {
            finish()
        }
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager // Для того чтобы спрятать клаву

        savedInstanceState?.let {     // Проверяем, есть ли сохранённый текст в эдит тексте
            val savedText = it.getString(keyForWatcher)
            if (savedText != null) {
                searchEditText.setText(savedText)
            }
        }
        searchEditText.setOnClickListener {
            searchEditText.requestFocus() // установка фокуса на эдиттекст
            inputMethodManager.showSoftInput(
                searchEditText,
                0
            )  // Появление клавиатуры при нажатии на эдиттекст
        }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //  empty
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                logicClearIc(p0)

                if (!p0.isNullOrEmpty()) {


                    // инициализ переменную таск в текст ватчере, иначе происходит вылет
                    txtForSearch = searchEditText.text.toString()
                    tvMsgSearch.makeGone()
                    btCleanHistory.makeGone()
                    recyclerView.makeGone()
//                    searchTracks(txtForSearch) // в интеракторе поиск настроен на поиск через 2 секунды
                    viewModel.searchTracks(txtForSearch)
                    phForNothingToShow.makeGone()
                    recyclerView.makeGone()
                    msgTopTxt.makeGone()
                    msgBotTxt.makeGone()
                    buttonNoInternet.makeGone()

                }
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
        var clearEditText: EditText =  // инициализирую эдиттекст
            findViewById<AppCompatEditText>(R.id.search_stroke)
        // Извлечение данных из Bundle
        val savedText = savedInstanceState.getString(keyForWatcher)
        if (savedText != null) {
            clearEditText.setText(savedText)
        }
    }

    private fun logicClearIc(s: CharSequence?) {
        searchEditText =  // инициализирую эдиттекст
            findViewById<AppCompatEditText>(R.id.search_stroke)
        if (!s.isNullOrBlank()) {  // Перенести в функцию
            searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                ContextCompat.getDrawable(this@SearchActivity, R.drawable.ic_hintsearch_16),
                null,
                ContextCompat.getDrawable(this@SearchActivity, R.drawable.ic_clear_16),
                null
            )
            textFromInput = s.toString()

        } else {
            searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                ContextCompat.getDrawable(this@SearchActivity, R.drawable.ic_hintsearch_16),
                null,
                null,
                null
            )
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clearTextFromEditText() { // метод очистки текста в эдиттексте
        searchEditText =
            findViewById<AppCompatEditText>(R.id.search_stroke)
        val inputMethodManager =
            getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        searchEditText.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEndBounds = searchEditText.compoundDrawables[2]?.bounds
                if (drawableEndBounds != null) {
                    val x = event.x.toInt()
                    val y = event.y.toInt()
                    if (x >= (view.width - (drawableEndBounds.width() + view.paddingRight)) &&
                        x <= view.width - view.paddingRight && y >= 0 && y <= view.height
                    ) {
                        searchEditText.text?.clear()
                        inputMethodManager.hideSoftInputFromWindow(
                            currentFocus?.windowToken,
                            0
                        ) // Прячем клаву
                        // чистим эдит текст
                        recyclerView.makeInvisible() // убрали список треков при очистке эдиттекста
                        msgTopTxt.makeGone()  //Убрали сообщение топ
                        msgBotTxt.makeGone() // убрали сообщение бот
                        phForNothingToShow.makeGone() // убрали плейсхолдер
                        buttonNoInternet.makeGone() // убрали кнопку
                        btCleanHistory.makeInvisible()
                        tvMsgSearch.makeInvisible()
                        searchEditText.clearFocus() // убираем фокус с эдиттекста чтобы при нажатии снова появился фокус + история поиска
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }


    // Вспомогательные методы
    private fun handleNoResults() {
        val phNts = ContextCompat.getDrawable(this, R.drawable.ph_nothing_to_show_120)
        phForNothingToShow.setImageDrawable(phNts)
        phForNothingToShow.makeVisible()
        msgTopTxt.makeVisible()
        msgTopTxt.text = getString(R.string.msg_nothing_to_show)
        tvMsgSearch.makeGone()
        btCleanHistory.makeGone()
    }

    private fun displayFavoriteTracks() {
        tvMsgSearch.makeVisible()
        btCleanHistory.makeVisible()
        phForNothingToShow.makeGone()
        msgBotTxt.makeGone()
        msgTopTxt.makeGone()
        buttonNoInternet.makeGone()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = favoriteAdapter
        recyclerView.makeVisible()
    }


    private fun displayTracks(tracks: List<Track>) {
        tvMsgSearch.makeGone()
        btCleanHistory.makeGone()
        phForNothingToShow.makeGone()
        msgBotTxt.makeGone()
        msgTopTxt.makeGone()
        buttonNoInternet.makeGone()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TrackAdapter(tracks, this)
        recyclerView.makeVisible()
    }

    private fun handleNoInternetConnection() {
        val phNts = ContextCompat.getDrawable(this, R.drawable.ph_no_internet_120)
        phForNothingToShow.setImageDrawable(phNts)
        phForNothingToShow.makeVisible()
        msgTopTxt.makeVisible()
        msgTopTxt.text = getString(R.string.msg_no_internet_top)
        msgBotTxt.makeVisible()
        msgBotTxt.text = getString(R.string.msg_no_internet_bottom)
        buttonNoInternet.makeVisible()
        tvMsgSearch.makeGone()
        btCleanHistory.makeGone()
    }

    private fun View.makeGone() {
        this.visibility = View.GONE // функция для вью гон
    }

    private fun View.makeVisible() {
        this.visibility = View.VISIBLE // функция для вью визибл
    }

    private fun View.makeInvisible() {
        this.visibility = View.INVISIBLE // функция для вью инвизибл
    }

    override fun onTrackClicked(track: Track) { // переопределили метод onTrackClicked из интерфейса
        // Логика обработки нажатия на конкретный трек
        if (trackInteractor.clickDebounce()) { //если нажали более 1 раза за секунду не сработает
            trackInteractor.getTrackIntentAndStart(track, this)
            favoriteTrackInteractor.addTrack(track)
        }
        // вызову функцию и передам путэкстра

    }


    // Слушатель для отслеживания изменений в SharedPreferences
    private val sharedPrefListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == FavoriteTrackRepositoryImpl.Companion.TRACKS_KEY) {
                // Логика обновления треков
                updateTracksFromStorage()
            }
        }




    @SuppressLint("SuspiciousIndentation")
    private fun updateTracksFromStorage() {
        myTracks = favoriteTrackInteractor.getAllTracksFromStorage()
        favoriteAdapter.updateData(myTracks as MutableList<Track>)
    }



}