package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.main.viewModel.MainViewModel
import com.example.playlistmaker.presentation.media.viewmodel.AddPlayListViewModel
import com.example.playlistmaker.presentation.media.viewmodel.FavoriteFragmentViewModel
import com.example.playlistmaker.presentation.media.viewmodel.MediaFragmentViewModel
import com.example.playlistmaker.presentation.media.viewmodel.PlaylistFragmentViewModel
import com.example.playlistmaker.presentation.media.viewmodel.PlaylistTracksViewModel
import com.example.playlistmaker.presentation.media.viewmodel.ReplacePlaylistViewModel
import com.example.playlistmaker.presentation.player.viewModel.PlayerViewModel
import com.example.playlistmaker.presentation.search.viewModel.SearchViewModel
import com.example.playlistmaker.presentation.settings.viewModel.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {


    viewModel { // описать получение юз кейсов в интерактор модуле
        MainViewModel(get())
    }
    viewModel { // описать получение юз кейсов в интерактор модуле
        FavoriteFragmentViewModel(get())
    }



//Ниже рефакторинг на фрагмент


    viewModel { // описать получение юз кейсов в интерактор модуле
        MediaFragmentViewModel()
    }


    viewModel { params ->
        PlayerViewModel(
            get(),
            params.get(),
            get(),
            get()// в качестве парамс тут интент передаю в активити
            )
    }


    viewModel {
        SearchViewModel( get(), get())
    }




    viewModel { // описать получение юз кейсов в интерактор модуле
        SettingsViewModel(get(), get(), get(), get())
    }


    viewModel {
        PlaylistFragmentViewModel( get())
    }

    viewModel {
        AddPlayListViewModel( get())
    }

    viewModel { params->
        ReplacePlaylistViewModel( get(), params.get())
    }



    viewModel { params->
        PlaylistTracksViewModel( get(), params.get())
    }

}





