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
    private var currentPage: Int = 0

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
if (savedInstanceState == null) {
    val pager = binding.viewpager
    var adapter = PagerAdapter(supportFragmentManager, lifecycle)
    pager.adapter = adapter

    TabLayoutMediator(binding.tabLayout, pager) { tab, position ->
        when (position) {
            0 -> {
                tab.text = getString(R.string.tab1txt)
                currentPage = 0
            }

            1 -> {
                tab.text = getString(R.string.tab2txt)
currentPage = 1
            }

        }


    }.attach()
}
    }


    override fun onResume() {
        super.onResume()

/*
Зарегали адаптер вью пейджера ( он обязателен)
 */
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Сохраняем флаг логики, чтобы в будущем мы могли его восстановить
        outState.putInt("tabPosition", currentPage)
    }
}