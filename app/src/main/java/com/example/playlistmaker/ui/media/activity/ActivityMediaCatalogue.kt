package com.example.playlistmaker.ui.media.activity

import ActivityMediaCatalogueViewModel
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaCatalogueBinding
import com.example.playlistmaker.ui.media.vp2adapter.PagerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityMediaCatalogue : AppCompatActivity() {
    companion object {
        const val savedPage = "savedPage"
    }

    private lateinit var binding: ActivityMediaCatalogueBinding
    private val viewModel by viewModel<ActivityMediaCatalogueViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMediaCatalogueBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_media_catalogue)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.searchToolbar.setNavigationOnClickListener { finish() }
        viewModel.controlThemeInOtherWindows()

        setupTabsAndPager(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Сохраняем текущее положение вкладки
        outState.putInt(savedPage, binding.viewpager.currentItem)
    }

    private fun setupTabsAndPager(savedInstanceState: Bundle?) {
        val pager = binding.viewpager
        val adapter = PagerAdapter(supportFragmentManager, lifecycle)
        pager.adapter = adapter

        savedInstanceState?.let {
            pager.currentItem = it.getInt(savedPage, 0)
        }

        TabLayoutMediator(binding.tabLayout, pager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab1txt)
                1 -> tab.text = getString(R.string.tab2txt)
            }
        }.attach()
    }
}