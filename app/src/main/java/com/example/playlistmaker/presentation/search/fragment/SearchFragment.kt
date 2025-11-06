package com.example.playlistmaker.presentation.search.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.search.listener.OnTrackClickListener
import com.example.playlistmaker.presentation.search.viewModel.SearchViewModel
import com.example.playlistmaker.presentation.theme.ThemeViewModel
import com.example.playlistmaker.ui.PlaylistMakerTheme
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SearchFragment : Fragment(), OnTrackClickListener {
    private val viewModel: SearchViewModel by activityViewModel()
    private val themeViewModel: ThemeViewModel by activityViewModel()
    private lateinit var searchDebounce: (String) -> Unit
    private lateinit var trackClickDebounce: (Track) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Обязательно: стратегия уничтожения композиции
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@SearchFragment
                )
            )

            setContent {
                val themeMode = themeViewModel.themeMode.observeAsState()
                PlaylistMakerTheme(themeMode as State<Boolean>) {
                    SearchScreen(
                        viewModel,
                        onSearchTextChanged = { text ->
                            if (text.isNotEmpty()) {
                                searchDebounce(text)
                            }
                        },
                        onRetryClick = { text ->
                            // При retry вызываем поиск напрямую без debounce
                            if (text.isNotEmpty()) {
                                viewModel.searchTracks(text)
                            }
                        },
                        loadSearchHistory = {
                            viewModel.getAllTracks()
                        },
                        onTrackClick = { onTrackClicked(track = it)}
                    )
                }
            }

        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchDebounce =
            debounce(2000L, viewLifecycleOwner.lifecycleScope, true) { text ->
                viewModel.searchTracks(text)
            }
        trackClickDebounce =
            debounce(100L, viewLifecycleOwner.lifecycleScope, false) { track ->
                viewModel.addTrackToFavorite(track)

            }

        }

    override fun onTrackClicked(track: Track) { // переопределили метод onTrackClicked из интерфейса
        // Логика обработки нажатия на конкретный трек

        getTrackIntentAndStart(track, requireContext())
        trackClickDebounce(track)
        viewModel.addTrackToFavorite(track)

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


