package com.example.playlistmaker.ui.media.activity


import ActivityMediaCatalogueViewModel
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMediaCatalogueBinding
import com.example.playlistmaker.ui.media.fragments.FavoriteTrakListFragment
import com.example.playlistmaker.ui.media.vp2adapter.PagerAdapter
import com.google.android.material.tabs.TabLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class ActivityMediaCatalogue : AppCompatActivity() {

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

        binding.searchToolbar.setNavigationOnClickListener {  //назад в Майнактивити
            finish()
        }
        viewModel.controlThemeInOtherWindows()




    }


    override fun onResume() {
        super.onResume()

        val pager = binding.viewpager
        var adapter = PagerAdapter(supportFragmentManager, lifecycle)
        pager.adapter = adapter
/*
Зарегали адаптер вью пейджера ( он обязателен)
 */


    }
}