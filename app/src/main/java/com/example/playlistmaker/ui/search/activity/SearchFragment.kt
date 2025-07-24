package com.example.playlistmaker.ui.search.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.ui.search.adapters.TrackAdapter
import com.example.playlistmaker.ui.search.listener.OnTrackClickListener
import com.example.playlistmaker.ui.search.viewModel.SearchViewModel
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SearchFragment : Fragment(), OnTrackClickListener {
    private val viewModel: SearchViewModel by activityViewModel()
    private lateinit var searchEditText: AppCompatEditText
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
    private lateinit var pbs: ProgressBar
    private lateinit var binding: FragmentSearchBinding

    private lateinit var searchDebounce: (String) -> Unit
    private lateinit var trackClickDebounce: (Track) -> Unit
    private lateinit var oldText: CharSequence
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState?.getString(keyForWatcher) != null) {
            var clearEditText: EditText =  // инициализирую эдиттекст
                binding.searchStroke
            // Извлечение данных из Bundle
            val savedText = savedInstanceState.getString(keyForWatcher)
            if (savedText != null) {
                clearEditText.setText(savedText)
            }


        }



        searchEditText =  // инициализирую эдиттекст
            binding.searchStroke


        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            viewModel.getAllTracks()
            observeTrackSearchResults(hasFocus)
            // Наблюдаем сразу за обоими источниками данных

        }






        pbs = binding.pbs

        tvMsgSearch =
            binding.tvMsgSearch

        btCleanHistory =
            binding.btCleanHistory




        phForNothingToShow =
            binding.phNtsh120



        msgTopTxt =
            binding.msgNointTopTxt

        msgBotTxt =
            binding.msgNointBottomTxt

        buttonNoInternet =
            binding.buttonNointernet

        recyclerView =
            binding.trackList




        buttonNoInternet.setOnClickListener { // Кнопка поиска при отсутствии интернета
            phForNothingToShow.makeGone()
            recyclerView.makeGone()
            msgTopTxt.makeGone()
            msgBotTxt.makeGone()
            buttonNoInternet.makeGone()
            txtForSearch = searchEditText.text.toString() // текст для поиска
            viewModel.searchTracks(txtForSearch)

        }

        btCleanHistory.setOnClickListener {  // кнопка очистки истории
            viewModel.clearHistory()
            recyclerView.makeInvisible() // делаю ресайклер вью невидимым
            tvMsgSearch.makeInvisible() //делаем сообщение "Вы искали" невидимым
            btCleanHistory.makeInvisible() // делаем саму кнопку невидимой при выполнении логики
            searchEditText.clearFocus()  // убираю фокус7
        }


        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager // Для того чтобы спрятать клаву

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
        searchDebounce =
            debounce<String>(2000L, viewLifecycleOwner.lifecycleScope, true) { txtForSearch ->
                viewModel.searchTracks(txtForSearch)
            }

        trackClickDebounce =
            debounce<Track>(100L, viewLifecycleOwner.lifecycleScope, false) { track ->
                viewModel.addTrackToFavorite(track)

            }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                oldText = p0 ?: ""

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                logicClearIc(p0)
                val currentText = p0 ?: ""
                if (!p0.isNullOrEmpty()) {
                    // инициализ переменную таск в текст ватчере, иначе происходит вылет
                    txtForSearch = p0.toString()
                    tvMsgSearch.makeGone()
                    btCleanHistory.makeGone()
                    recyclerView.makeInvisible()
                    if (currentText == oldText) {
                        /*
                        C помощью текст ватчера проверяю изменился ли текст после возвращения через popBackStack()
                        и выполняю поисковый запрос только при наличии изменений ( убрал неприятный прогресс бар при возврате на экран -
                        - появлялся на пару секунд выполняя повторный запрос)
                         */
                        searchDebounce(txtForSearch)
                    }
//                    viewModel.searchTracks(txtForSearch)
                    phForNothingToShow.makeGone()

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


    private fun logicClearIc(s: CharSequence?) {
        searchEditText =  // инициализирую эдиттекст
            binding.searchStroke
        if (!s.isNullOrBlank()) {  // Перенести в функцию
            searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_hintsearch_16),
                null,
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear_16),
                null
            )
            textFromInput = s.toString()

        } else {
            searchEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_hintsearch_16),
                null,
                null,
                null
            )
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clearTextFromEditText() { // метод очистки текста в эдиттексте
        searchEditText =
            binding.searchStroke
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
                            view?.windowToken,
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

    private fun observeTrackSearchResults(hasFocus: Boolean) {
        viewModel.getLiveData.observe(viewLifecycleOwner) { newState ->
            when {
                newState.isLoading -> pbs.makeVisible()
                !newState.errorMessage.isNullOrEmpty() && !newState.isLoading && hasFocus && searchEditText.text?.isNullOrEmpty() == false -> {
                    handleNoInternetConnection()
                    pbs.makeGone()
                }

                newState.searchResults.isNullOrEmpty() && newState.errorMessage == null && hasFocus && searchEditText.text?.isNullOrEmpty() == false -> {
                    pbs.makeGone()
                    handleNoResults()
                }

                else -> {
                    pbs.makeGone()
                    if (hasFocus && searchEditText.text.isNullOrEmpty()) {
                        val tracksToDisplay = newState.history
                        tracksToDisplay?.let { displayTracks(it) }
                        btCleanHistory.makeVisible()
                        if (!tracksToDisplay.isNullOrEmpty()) {
                            btCleanHistory.makeVisible()
                        } else btCleanHistory.makeGone()

                    } else if (!searchEditText.text.isNullOrEmpty()) {
                        val tracksToDisplay = newState.searchResults
                        tracksToDisplay?.let { displayTracks(it) }
                    } else {
                        recyclerView.makeInvisible()
                        btCleanHistory.makeGone()
                        phForNothingToShow.makeGone()
                        msgTopTxt.makeInvisible()
                        msgBotTxt.makeInvisible()


                    }

                }
            }
        }
    }


    // Вспомогательные методы
    private fun handleNoResults() {
        val phNts = ContextCompat.getDrawable(requireContext(), R.drawable.ph_nothing_to_show_120)
        phForNothingToShow.setImageDrawable(phNts)
        phForNothingToShow.makeVisible()
        msgTopTxt.makeVisible()
        msgTopTxt.text = getString(R.string.msg_nothing_to_show)
        tvMsgSearch.makeGone()
        btCleanHistory.makeGone()
    }
//


    private fun displayTracks(tracks: List<Track>) {
        tvMsgSearch.makeGone()
        btCleanHistory.makeGone()
        phForNothingToShow.makeGone()
        msgBotTxt.makeGone()
        msgTopTxt.makeGone()
        buttonNoInternet.makeGone()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = TrackAdapter(tracks, this)
        recyclerView.makeVisible()
    }

    private fun handleNoInternetConnection() {
        val phNts = ContextCompat.getDrawable(requireContext(), R.drawable.ph_no_internet_120)
        phForNothingToShow.setImageDrawable(phNts)
        phForNothingToShow.makeVisible()
        msgTopTxt.makeVisible()
        msgTopTxt.text = getString(R.string.msg_no_internet_top)
        msgBotTxt.makeVisible()
        msgBotTxt.text = getString(R.string.msg_no_internet_bottom)
        buttonNoInternet.makeVisible()
        tvMsgSearch.makeGone()
        btCleanHistory.makeGone()
        recyclerView.makeInvisible()
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

        getTrackIntentAndStart(track, requireContext())
        trackClickDebounce(track)
        viewModel.addTrackToFavorite(track)

    }

    override fun onResume() {
        super.onResume()


        observeTrackSearchResults(true)


    }


    override fun onDestroy() {
        super.onDestroy()
        viewModel.getLiveData.removeObservers(this) // удалил обсервер вью модели поиска треков
    }


    fun getTrackIntentAndStart(track: Track, context: Context) {
        val bundle = Bundle().apply {
            putSerializable("track", track)

        }
        findNavController().navigate(R.id.action_searchFragment_to_playerFragment, bundle)

    }


}


