package com.example.playlistmaker.di

import ActivityMediaCatalogueViewModel
import com.example.playlistmaker.ui.main.viewModel.MainViewModel
import com.example.playlistmaker.ui.media.viewmodel.FavoriteFragmentViewModel
import com.example.playlistmaker.ui.media.viewmodel.MediaFragmentViewModel
import com.example.playlistmaker.ui.media.viewmodel.PlaylistFragmentViewModel
import com.example.playlistmaker.ui.player.viewModel.PlayerViewModel
import com.example.playlistmaker.ui.player.viewModel.PlayerViewModelDepricate
import com.example.playlistmaker.ui.search.viewModel.SearchViewModel
import com.example.playlistmaker.ui.search.viewModel.SearchViewModel2
import com.example.playlistmaker.ui.settings.viewModel.SettingsViewModel
import com.example.playlistmaker.ui.settings.viewModel.SettingsViewModel2
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
            PlayerViewModelDepricate(
                get(),
                get(),
                get(),
                get(),
                params.get()  // в качестве парамс тут интент передаю в активити
            )
        }

        viewModel{ // описать получение юз кейсов в интерактор модуле
            ActivityMediaCatalogueViewModel(get())
        }

        viewModel{ // описать получение юз кейсов в интерактор модуле
            FavoriteFragmentViewModel(get())
        }
        viewModel{ // описать получение юз кейсов в интерактор модуле
            PlaylistFragmentViewModel(get())
        }

//Ниже рефакторинг на фрагмент


            viewModel{ // описать получение юз кейсов в интерактор модуле
                MediaFragmentViewModel(get())
            }



        viewModel { params ->
            PlayerViewModel(
                get(),
                get(),
                get(),
                get(),
                params.get()  // в качестве парамс тут интент передаю в активити
            )
        }


        viewModel{
            SearchViewModel2(get(),get(),get())
        }




        viewModel{ // описать получение юз кейсов в интерактор модуле
            SettingsViewModel2(get(),get(), get(),get())
        }

    }


