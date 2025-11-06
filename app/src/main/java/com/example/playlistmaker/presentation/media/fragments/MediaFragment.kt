package com.example.playlistmaker.presentation.media.fragments

import android.annotation.SuppressLint
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
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.media.viewmodel.FavoriteFragmentViewModel
import com.example.playlistmaker.presentation.media.viewmodel.MediaFragmentViewModel
import com.example.playlistmaker.presentation.media.viewmodel.PlaylistFragmentViewModel
import com.example.playlistmaker.presentation.theme.ThemeViewModel
import com.example.playlistmaker.ui.PlaylistMakerTheme
import com.example.playlistmaker.utils.debounce
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MediaFragment : Fragment() {

    private val mediaFragmentViewModel: MediaFragmentViewModel by activityViewModel()
    private val favoriteFragmentViewModel: FavoriteFragmentViewModel by activityViewModel()
    private val playlistFragmentViewModel: PlaylistFragmentViewModel by activityViewModel()
    private val themeViewModel: ThemeViewModel by activityViewModel()

    private lateinit var trackClickDebounce: (Track) -> Unit



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Обязательно: стратегия уничтожения композиции
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnLifecycleDestroyed(
                    lifecycleOwner = this@MediaFragment
                )
            )

            setContent {
                val themeMode = themeViewModel.themeMode.observeAsState()
                PlaylistMakerTheme(themeMode as State<Boolean>) {
                    MediaScreen(
                        onTrackClick = { onTrackClicked(it) },
                        onPlayListClick = { onPlaylistClicked(it) },
                        favoriteFragmentViewModel = favoriteFragmentViewModel,
                        playlistFragmentViewModel = playlistFragmentViewModel,
                        onAddPlayListClick = {
                            findNavController().navigate(R.id.action_mediaFragment_to_addPlayListFragment)


                        }, mediaFragmentViewModel = mediaFragmentViewModel
                    )

                }
            }

            }
    }
    @SuppressLint("SetTextI18n")
   private fun onPlaylistClicked(playList: PlayList) {
        try {
            val bundle = Bundle().apply {
                playList.listId?.let { putInt("ID1", it) }

            }
            findNavController().navigate(R.id.playlistTracksFragment, bundle)
        } catch(e: Exception){
        }
    }
     private fun onTrackClicked(track: Track) { // переопределили метод onTrackClicked из интерфейса
        // Логика обработки нажатия на конкретный трек

        getTrackIntentAndStart(track, requireContext())
        trackClickDebounce(track)


    }
    private fun getTrackIntentAndStart(track: Track, context: Context) {
        val bundle = Bundle().apply {
            putSerializable("track", track)

        }
        findNavController().navigate(R.id.playerFragment, bundle)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistFragmentViewModel.getAllPlaylists()

        trackClickDebounce =
            debounce(100L, viewLifecycleOwner.lifecycleScope, false) { track ->

            }

    }

}