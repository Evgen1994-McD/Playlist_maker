package com.example.playlistmaker.presentation.settings.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.example.playlistmaker.presentation.settings.viewModel.SettingsViewModel
import com.example.playlistmaker.ui.PlaylistMakerTheme
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class SettingsFragment : Fragment() {

    private var isUpdatingUI = false


    private val viewModel: SettingsViewModel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            // Обязательно: стратегия уничтожения композиции
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(lifecycleOwner = this@SettingsFragment))

            setContent {
                PlaylistMakerTheme(viewModel = viewModel){


                    SettingsScreen(viewModel)

                }
            }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        val shareTheAppClicker = // Кликер поделиться приложением
//            binding.shareApp
//        shareTheAppClicker.setOnClickListener {
//            shareApp()
//        }
//        val mesToSuppClicker = // Пишем в поддержку
//            binding.sMesToSuport
//        mesToSuppClicker.setOnClickListener {
//            sendSuppEmail()  //Здесь будет вызван метод
//        }
//        val userAssetClicker =
//            binding.userAssetUri
//        userAssetClicker.setOnClickListener {
//            openUrlInDefaultBrowser()
//        }
//        val switcherTheme = binding.switchTheme


// Установка обработчика изменений

//        switcherTheme.isChecked = viewModel.getLiveData.value as Boolean
//
//        switcherTheme.setOnCheckedChangeListener { _, isChecked ->
//            if (!isUpdatingUI) {
//                viewModel.controlTHemeBySwitcher(isChecked)
//            }
//        }


    }

    fun shareApp() {  // Метод - интент для отправки сообщений
        viewModel.shareApp()

    }

    private fun sendSuppEmail() {  // Приватный метод для письма в поддержку
        viewModel.sendSuppEmail()
    }

    private fun openUrlInDefaultBrowser() {
        viewModel.openUrlInDefaultBrowser()
    }

    override fun onDestroy() { // закрываем плеер при завершении работы
        super.onDestroy()
        viewModel.getLiveData.removeObservers(this)

    }


}


