package com.example.playlistmaker.ui.settings.activity

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.databinding.SettingsBinding
import com.example.playlistmaker.ui.settings.viewModel.SettingsViewModel
import com.example.playlistmaker.ui.settings.viewModel.SettingsViewModel2
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private var isUpdatingUI = false

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel2 by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)





        val shareTheAppClicker = // Кликер поделиться приложением
            binding.shareApp
        shareTheAppClicker.setOnClickListener {
            shareApp()
        }
        val mesToSuppClicker = // Пишем в поддержку
            binding.sMesToSuport
        mesToSuppClicker.setOnClickListener {
            sendSuppEmail()  //Здесь будет вызван метод
        }
        val userAssetClicker =
            binding.userAssetUri
        userAssetClicker.setOnClickListener {
            openUrlInDefaultBrowser()
        }
        val switcherTheme = binding.switchTheme



// Подписка на получение изменений из LiveData
        viewModel.getLiveData.observe(viewLifecycleOwner) { currentTheme ->
            if (currentTheme != switcherTheme.isChecked && !isUpdatingUI) {
                isUpdatingUI = true // Блокируем UI-обновления на время операции
                switcherTheme.isChecked = currentTheme
                isUpdatingUI = false // Разрешаем последующие обновления
            }
        }

// Установка обработчика изменений
        switcherTheme.setOnCheckedChangeListener { _, isChecked ->
            if (!isUpdatingUI) {
                viewModel.controlTHemeBySwitcher(isChecked)
            }
        }


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


