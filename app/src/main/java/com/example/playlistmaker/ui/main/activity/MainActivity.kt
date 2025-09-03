package com.example.playlistmaker.ui.main.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.ui.main.viewModel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val viewModelMain by viewModel<MainViewModel>()
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController:NavController
    private lateinit var bottomNavigationView:BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        viewModelMain.controlThemeInOtherWindows()

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)
        /*

Настройка боттом навигации в активити
1.  Используем supportFragmentManager, чтобы найти NavHostFragment.
2. Получаем экземпляр NavController у найденного навигационного фрагмента.
3 Далее  надо лишь найти на данном экране BottomNavigationView и передать ему NavController при помощи простого метода setupWithNavController:
 */

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.searchFragment -> navigateAndClearOldFragments(R.id.searchFragment)
                R.id.mediaFragment -> navigateAndClearOldFragments(R.id.mediaFragment)
                R.id.settingsFragment -> navigateAndClearOldFragments(R.id.settingsFragment)
                else -> false
            }
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
R.id.addPlayListFragment -> bottomNavigationView.isVisible = false
R.id.playlistTracksFragment -> bottomNavigationView.isVisible = false
R.id.action_playlistTracksFragment_to_replacePlayListFragment -> bottomNavigationView.isVisible = false
                R.id.replacePlayListFragment->bottomNavigationView.isVisible = false
                else-> bottomNavigationView.isVisible = true
            }


        }
    }

//
    private  fun navigateAndClearOldFragments(destinationId: Int): Boolean {
        // Чистим стек навигации и переходим на указанный пункт
        navController.popBackStack()
        navController.navigate(destinationId)
        return true
    }



    override fun onDestroy() {
        super.onDestroy()

    }

}