package com.example.playlistmaker.ui.media.fragments

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentMediaBinding
import com.example.playlistmaker.ui.media.activity.ActivityMediaCatalogue.Companion.savedPage
import com.example.playlistmaker.ui.media.viewmodel.MediaFragmentViewModel
import com.example.playlistmaker.ui.media.vp2adapter.VpAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MediaFragment : Fragment() {
    private lateinit var binding: FragmentMediaBinding
    private val pager by lazy { binding.viewpager }
    private val tabs by lazy { binding.tabLayout }
    private val vpAdapter by lazy { VpAdapter(this) }
    private val mediaFragmentViewModel: MediaFragmentViewModel by activityViewModel()
    private var currentPagePosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем сохранённую позицию вкладки
        savedInstanceState?.let {
            currentPagePosition = it.getInt(savedPage, 0)
        }

        setupTabsAndPager()

        // Следим за сменой вкладок
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                currentPagePosition = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем текущее положение вкладки
        outState.putInt(savedPage, currentPagePosition)
    }

    private fun setupTabsAndPager() {
        pager.adapter = vpAdapter
        pager.currentItem = currentPagePosition

        TabLayoutMediator(tabs, pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab1txt)
                1 -> tab.text = getString(R.string.tab2txt)
            }
        }.attach()
    }
}