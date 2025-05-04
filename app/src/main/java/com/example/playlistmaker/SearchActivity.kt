package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
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
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.ui.adapters.FavoriteTrackAdapter
import com.example.playlistmaker.ui.adapters.TrackAdapter
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.data.network.ITunesApi
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.models.TrackResponse
import com.example.playlistmaker.utils.Constants
import com.example.playlistmaker.utils.OnTrackClickListener
import kotlinx.coroutines.Runnable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import retrofit2.Callback


class SearchActivity : AppCompatActivity(),
    OnTrackClickListener {  // Добавили имлементацию нашего интерфейса OnTrackClickListener для того чтобы определить трек
    private lateinit var clearEditText: AppCompatEditText
    private lateinit var binding: ActivitySearchBinding // делаю байдинг

    private lateinit var txtForSearch: String
    private var textFromInput: String = null.toString()
    private val keyForWatcher: String =
        "keyForWatcherSearch"  // Константа для ватчера
    private lateinit var phForNothingToShow: ImageView
    private lateinit var msgTopTxt: TextView
    private lateinit var msgBotTxt: TextView
    private lateinit var buttonNoInternet: TextView
    private val iTunesBaseUrl = "https://itunes.apple.com"
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvMsgSearch: TextView
    private lateinit var btCleanHistory: TextView
    private lateinit var storage: TrackStorage
    private lateinit var myTracks: List<Track>
    private lateinit var myLikeAdapter: FavoriteTrackAdapter //адаптер будущий
    private lateinit var pbs: ProgressBar
    private val handler =
        Handler(Looper.getMainLooper()) // Сделал Хандлер для доступа к главному потоку
private lateinit var task : Runnable // задача для потока для того чтобы сделать onDebounce
    @SuppressLint("ClickableViewAccessibility", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val sharedPrefs =
            getSharedPreferences(Constants.SHARED_PREF_THEME_NAME, Context.MODE_PRIVATE)
        val theme = applicationContext as App  // загрузка сохранённой темы в SharedPreferences
        if (theme.hasBooleanValue(this@SearchActivity, Constants.KEY_THEME_MODE)) {
            var savedTheme = sharedPrefs.getBoolean(Constants.KEY_THEME_MODE, false)
            theme.switchTheme(savedTheme)
        } else {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

        val sharedprefs = getSharedPreferences(
            TrackStorage.PREFS_NAME,
            MODE_PRIVATE
        ) //Объявили sharedPreferences для подписки в дальнейшем на обновления

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.searchLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        pbs = findViewById<ProgressBar>(R.id.pbs)

        tvMsgSearch =
            findViewById<TextView>(R.id.tv_msg_search)

        btCleanHistory =
            findViewById<TextView>(R.id.bt_cleanHistory)




        clearEditText =  // инициализирую эдиттекст
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
            phForNothingToShow.makeGone()
            recyclerView.makeGone()
            msgTopTxt.makeGone()
            msgBotTxt.makeGone()
            buttonNoInternet.makeGone()
            txtForSearch = clearEditText.text.toString() // текст для поиска
            searchSongs(
                txtForSearch,
                iTunesApi
            ) // передаю параметры для поиска в метод. Пробую передать ту же логику, что и в поиске, ведь кнопку будет видно только при определенных условиях

        }

        storage = TrackStorage(this@SearchActivity) // инициализируем экземпляр класса Trackstorage
        myTracks = storage.getAllTracks() //все треки
        sharedprefs.registerOnSharedPreferenceChangeListener(sharedPrefListener) //регистрируем слушатель изменений на наш sharedprefs чтобы сразу подгрузить изменения в список адаптера
        myLikeAdapter =
            FavoriteTrackAdapter(
                storage.getAllTracks() as MutableList<Track>?,
                this@SearchActivity
            ) // инициализировали фаворит адаптер
// передаю this@searchactivity потому что активити имплементирует интерфейс

        btCleanHistory.setOnClickListener {  // кнопка очистки истории
            storage.clearHistory()  //очищаю историю, метод прописан в классе TrackStorage
            recyclerView.makeInvisible() // делаю ресайклер вью невидимым
            tvMsgSearch.makeInvisible() //делаем сообщение "Вы искали" невидимым
            btCleanHistory.makeInvisible() // делаем саму кнопку невидимой при выполнении логики
            clearEditText.clearFocus()  // убираю фокус
        }
        clearEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && clearEditText.text?.isNullOrEmpty() == true && !storage.getAllTracks()
                    .isNullOrEmpty()
            ) {    // таким образом, вызываю подсказку "вы искали" только когда соблюдаем : (фокус + текст пуст + сторейждж не пуст)
                tvMsgSearch.makeVisible()
                recyclerView.makeVisible()
                btCleanHistory.makeVisible()
                recyclerView.layoutManager = LinearLayoutManager(this@SearchActivity)
                recyclerView.adapter = myLikeAdapter
            }
        }


        val backClicker =
            findViewById<Toolbar>(R.id.search_toolbar) // Назад в MainActivity
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
            clearEditText.requestFocus() // установка фокуса на эдиттекст
            inputMethodManager.showSoftInput(
                clearEditText,
                0
            )  // Появление клавиатуры при нажатии на эдиттекст
        }

        clearEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //  empty
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                logicClearIc(p0)


                if (!p0.isNullOrEmpty()) {
                    task = Runnable{searchSongs(txtForSearch, iTunesApi)} // инициализ переменную таск в текст ватчере, иначе происходит вылет

                    tvMsgSearch.makeGone()
                    btCleanHistory.makeGone()
                    recyclerView.makeGone()
                    txtForSearch = clearEditText.text.toString()

                    searchDebounce() //  дебаунс автопоиск спустя 2 секунды
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
            findViewById<androidx.appcompat.widget.AppCompatEditText>(R.id.search_stroke)
        // Извлечение данных из Bundle
        val savedText = savedInstanceState.getString(keyForWatcher)
        if (savedText != null) {
            clearEditText.setText(savedText)
        }
    }

    private fun logicClearIc(s: CharSequence?) {
        clearEditText =  // инициализирую эдиттекст
            findViewById<AppCompatEditText>(R.id.search_stroke)
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
    private fun clearTextFromEditText() { // метод очистки текста в эдиттексте
        clearEditText =
            findViewById<AppCompatEditText>(R.id.search_stroke)
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
                        recyclerView.makeInvisible() // убрали список треков при очистке эдиттекста
                        msgTopTxt.makeGone()  //Убрали сообщение топ
                        msgBotTxt.makeGone() // убрали сообщение бот
                        phForNothingToShow.makeGone() // убрали плейсхолдер
                        buttonNoInternet.makeGone() // убрали кнопку
                        btCleanHistory.makeInvisible()
                        tvMsgSearch.makeInvisible()
                        clearEditText.clearFocus() // убираем фокус с эдиттекста чтобы при нажатии снова появился фокус + история поиска
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }
    }

private fun searchSongs(txtFromInput: String, iTunesApi: ITunesApi) {
    // Удаляем предыдущие задания (для предотвращения дублирования)
    handler.removeCallbacksAndMessages(null)

    // Создаем новый Runnable для выполнения поиска
    task = Runnable {
        runOnUiThread{
            pbs.makeVisible()
        }


        try {
            // Вызываем метод getSong, который теперь возвращает Call<TrackResponse>
            val call = iTunesApi.getSong(txtFromInput)

            // Ставим запрос в очередь и обрабатываем результат асинхронно
            call.enqueue(object : Callback<TrackResponse> {
                override fun onResponse(call: Call<TrackResponse>, response: Response<TrackResponse>) {

                    runOnUiThread {

                            pbs.makeGone()// убираем ПБ после запроса

                        if (response.isSuccessful) {

                            val trackResponse = response.body()
                            val tracks = trackResponse?.results

                            if (tracks.isNullOrEmpty()) {
                                handleNoResults()
                                pbs.makeGone()// убираем ПБ после запроса

                            } else {
                                pbs.makeGone()// убираем ПБ после запроса

                                displayTracks(tracks!!)


                            }
                        } else {
                            pbs.makeGone()// убираем ПБ после запроса

                            handleNoInternetConnection()
                        }
                    }
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    runOnUiThread {
                        pbs.makeGone()// убираем ПБ после запроса

                        handleNoInternetConnection()
                    }
                }
            })
        } catch (e: IOException) {
            runOnUiThread {
                pbs.makeGone()// убираем ПБ после запроса

                handleNoInternetConnection()
            }
        }
    }

    // Запускаем задачу в другом потоке
    Thread(task).start()
}

    private fun searchDebounce() {

        handler.removeCallbacks(task) //отменить колбек от таск
        handler.postDelayed(task, SEARCH_DEBOUNCE_DELAY) // поиск



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
        if (clickDebounce()) { //если нажали более 1 раза за секунду не сработает
            intentAndStartActivity(track)
            storage.addTrack(track)
        }
        // вызову функцию и передам путэкстра

    }



    private var isClickAllowed = true // переменная для дебаунс клика




    private fun clickDebounce() : Boolean {  //Дебаунс защита повторных нажатий на трек
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }



    private fun updateTracksFromStorage() { //обновляем треки из хранилища
        val updateTracks = storage.getAllTracks()
        myLikeAdapter.updateData(updateTracks as MutableList<Track>) //для этого мы прописали метот updateData в адаптере
    }


    // Слушатель для отслеживания изменений в SharedPreferences
    private val sharedPrefListener =
        SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == TrackStorage.TRACKS_KEY) {
                // Логика обновления треков
                updateTracksFromStorage()
            }
        }

    private fun intentAndStartActivity(track: Track) {
        val intent =
            Intent(this, MediaActivity::class.java) // создали интент для перехода на активити
        intent.putExtra("trackName", track.trackName)
        if (!track.collectionName.isNullOrEmpty()) {
            intent.putExtra(
                "collectionName",
                track.collectionName
            )  // отправим альбом только если он есть
        }
        intent.putExtra("trackTimeMillis", track.trackTimeMillis)
        intent.putExtra("artistName", track.artistName)
        intent.putExtra("primaryGenreName", track.primaryGenreName)
        intent.putExtra("country", track.country)
        intent.putExtra("artworkUrl100", track.artworkUrl100)
        intent.putExtra("previewUrl", track.previewUrl)

        intent.putExtra("relieseDate", track.releaseDate)
        startActivity(intent) // запускаем активити


    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L // время до начала автоматического поиска
        private const val CLICK_DEBOUNCE_DELAY = 1000L //время "блока" повторного нажатия на трек
    }

}
