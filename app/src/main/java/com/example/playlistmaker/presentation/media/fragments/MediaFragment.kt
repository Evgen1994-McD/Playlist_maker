package com.example.playlistmaker.presentation.media.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.example.playlistmaker.presentation.media.viewmodel.MediaFragmentViewModel
import com.example.playlistmaker.presentation.media.vp2adapter.VpAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MediaFragment : Fragment() {
    private lateinit var binding: FragmentMediaBinding
    private val mediaFragmentViewModel: MediaFragmentViewModel by activityViewModel()
    private var currentPagePosition:Int = 0
    private lateinit var vpAdapter: VpAdapter
    companion object {
        const val savedPageKey = "savedPage"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeCurrentTabs()

        val tabs = binding.tabLayout
        val pager = binding.viewpager
        val vpAdapter = VpAdapter(this)

        if (pager.adapter == null) {
            pager.adapter = vpAdapter
        }

            pager.currentItem = currentPagePosition
            tabs.selectTab(tabs.getTabAt(currentPagePosition))




        // Ждем окончания раскладки UI
        view.postDelayed({
            // Здесь гарантированно запустится после полного рендеринга

                pager.currentItem = currentPagePosition
                tabs.selectTab(tabs.getTabAt(currentPagePosition))

        }, 0) // Небольшая задержка для гарантии полноты отображения


        // Оповещаем ViewPager2 о наших страницах
        setupTabsAndPager(pager, tabs)

        // Уведомляем TabLayout о выбранной вкладке
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                pager.currentItem = tab.position
                mediaFragmentViewModel.setCurrentTabPosition(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // Callback для изменения страницы
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                tabs.selectTab(tabs.getTabAt(position))
            }
        })

        // Ограничиваем кэширование страниц
        pager.offscreenPageLimit = 1

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(savedPageKey, currentPagePosition)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        savedInstanceState?.let {
            currentPagePosition = it.getInt(savedPageKey, 0)
            binding.viewpager.currentItem = currentPagePosition
            binding.tabLayout.selectTab(binding.tabLayout.getTabAt(currentPagePosition))


        }
    }

    private fun setupTabsAndPager(pager: ViewPager2, tabs: TabLayout) {
        TabLayoutMediator(tabs, pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab1txt)
                1 -> tab.text = getString(R.string.tab2txt)
            }
        }.attach()
    }

    private fun observeCurrentTabs() {
        mediaFragmentViewModel.currentTabPosition.observe(viewLifecycleOwner) { position ->
            currentPagePosition = position


        }
    }
}