package com.example.playlistmaker.ui.media.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.example.playlistmaker.ui.media.viewmodel.MediaFragmentViewModel
import com.example.playlistmaker.ui.media.vp2adapter.VpAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MediaFragment : Fragment() {
    private lateinit var binding: FragmentMediaBinding
    private val mediaFragmentViewModel: MediaFragmentViewModel by activityViewModel()
    private var currentPagePosition = 0

    companion object {
        const val savedPageKey = "savedPage"
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

        // Получаем текущее положение вкладки из ViewModel
        mediaFragmentViewModel.currentTabPosition.observe(viewLifecycleOwner) { position ->
            currentPagePosition = position
            binding.viewpager.currentItem = position
        }

        val tabs = binding.tabLayout
        val pager = binding.viewpager
        val vpAdapter = VpAdapter(this)

        if (pager.adapter == null) {
            pager.adapter = vpAdapter
        }

        // Улучшаем производительность, позволяя предварительно загружать соседнюю страницу
        pager.offscreenPageLimit = 1

        setupTabsAndPager(pager, tabs)

        // Обработчик смены вкладок
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                mediaFragmentViewModel.setCurrentTabPosition(tab.position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем текущее положение вкладки
        outState.putInt(savedPageKey, currentPagePosition)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        // Восстанавливаем позицию вкладки при повороте экрана
        savedInstanceState?.let {
            currentPagePosition = it.getInt(savedPageKey, 0)
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
}