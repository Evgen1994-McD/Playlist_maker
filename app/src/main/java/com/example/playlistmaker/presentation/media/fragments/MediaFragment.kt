package com.example.playlistmaker.presentation.media.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.media.viewmodel.FavoriteFragmentViewModel
import com.example.playlistmaker.presentation.media.viewmodel.MediaFragmentViewModel
import com.example.playlistmaker.presentation.media.viewmodel.PlaylistFragmentViewModel
import com.example.playlistmaker.presentation.media.vp2adapter.VpAdapter
import com.example.playlistmaker.presentation.search.fragment.SearchFragment
import com.example.playlistmaker.presentation.theme.ThemeViewModel
import com.example.playlistmaker.ui.PlaylistMakerTheme
import com.example.playlistmaker.utils.debounce
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MediaFragment : Fragment() {

    private val mediaFragmentViewModel: MediaFragmentViewModel by activityViewModel()
    private val favoriteFragmentViewModel: FavoriteFragmentViewModel by activityViewModel()
    private val playlistFragmentViewModel: PlaylistFragmentViewModel by activityViewModel()
    private var currentPagePosition:Int = 0
    private lateinit var vpAdapter: VpAdapter
    private val themeViewModel: ThemeViewModel by activityViewModel()

    private lateinit var trackClickDebounce: (Track) -> Unit

    companion object {
        const val savedPageKey = "savedPage"
    }


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


                        }
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
//        observeCurrentTabs()
//
//        val tabs = binding.tabLayout
//        val pager = binding.viewpager
//        val vpAdapter = VpAdapter(this)
//
//        if (pager.adapter == null) {
//            pager.adapter = vpAdapter
//        }
//
//            pager.currentItem = currentPagePosition
//            tabs.selectTab(tabs.getTabAt(currentPagePosition))
//



//        // Ждем окончания раскладки UI
//        view.postDelayed({
//            // Здесь гарантированно запустится после полного рендеринга
//
//                pager.currentItem = currentPagePosition
//                tabs.selectTab(tabs.getTabAt(currentPagePosition))
//
//        }, 0) // Небольшая задержка для гарантии полноты отображения


//        // Оповещаем ViewPager2 о наших страницах
//        setupTabsAndPager(pager, tabs)
//
//        // Уведомляем TabLayout о выбранной вкладке
//        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                pager.currentItem = tab.position
//                mediaFragmentViewModel.setCurrentTabPosition(tab.position)
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })
//
//        // Callback для изменения страницы
//        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                tabs.selectTab(tabs.getTabAt(position))
//            }
//        })
//
//        // Ограничиваем кэширование страниц
//        pager.offscreenPageLimit = 1

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putInt(savedPageKey, currentPagePosition)
    }

//    override fun onViewStateRestored(savedInstanceState: Bundle?) {
//        super.onViewStateRestored(savedInstanceState)
//        savedInstanceState?.let {
//            currentPagePosition = it.getInt(savedPageKey, 0)
//            binding.viewpager.currentItem = currentPagePosition
//            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(currentPagePosition))
//
//
//        }
//    }

//    private fun setupTabsAndPager(pager: ViewPager2, tabs: TabLayout) {
//        TabLayoutMediator(tabs, pager) { tab, position ->
//            when (position) {
//                0 -> tab.text = getString(R.string.tab1txt)
//                1 -> tab.text = getString(R.string.tab2txt)
//            }
//        }.attach()
//    }

//    private fun observeCurrentTabs() {
//        mediaFragmentViewModel.currentTabPosition.observe(viewLifecycleOwner) { position ->
//            currentPagePosition = position
//
//
//        }
//    }
}