package com.example.playlistmaker.di

import com.example.playlistmaker.ui.main.viewModel.MainViewModel
import com.example.playlistmaker.ui.media.fragments.PlaylistFragment
import com.example.playlistmaker.ui.media.viewmodel.FavoriteFragmentViewModel
import com.example.playlistmaker.ui.media.viewmodel.MediaFragmentViewModel
import com.example.playlistmaker.ui.media.viewmodel.PlaylistFragmentViewModel
import com.example.playlistmaker.ui.player.viewModel.PlayerViewModel
import com.example.playlistmaker.ui.search.viewModel.SearchViewModel
import com.example.playlistmaker.ui.settings.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {


    viewModel { // описать получение юз кейсов в интерактор модуле
        MainViewModel(get())
    }
    viewModel { // описать получение юз кейсов в интерактор модуле
        FavoriteFragmentViewModel()
    }

    viewModel { // описать получение юз кейсов в интерактор модуле
        PlaylistFragmentViewModel()
    }


//Ниже рефакторинг на фрагмент


    viewModel { // описать получение юз кейсов в интерактор модуле
        MediaFragmentViewModel()
    }


    viewModel { params ->
        PlayerViewModel(
            get(),
            get(),
            params.get()  // в качестве парамс тут интент передаю в активити
        )
    }


    viewModel {
        SearchViewModel( get(), get())
    }




    viewModel { // описать получение юз кейсов в интерактор модуле
        SettingsViewModel(get(), get(), get(), get())
    }


}





