package com.example.playlistmaker.di

import com.example.playlistmaker.ui.main.viewModel.MainViewModel
import com.example.playlistmaker.ui.player.viewModel.MediaViewModel
import com.example.playlistmaker.ui.search.viewModel.SearchViewModel
import com.example.playlistmaker.ui.settings.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module



    val viewModelModule = module {
        viewModel{
            SearchViewModel(get(),get(),get())
        }

        viewModel{ // описать получение юз кейсов в интерактор модуле
            MainViewModel(get())
        }

        viewModel{ // описать получение юз кейсов в интерактор модуле
            SettingsViewModel(get(),get(), get(),get())
        }

        viewModel { params ->
            MediaViewModel(
                get(),
                get(),
                get(),
                get(),
                params.get()  // в качестве парамс тут интент передаю в активити
            )
        }
    }


