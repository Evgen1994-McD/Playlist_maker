package com.example.playlistmaker.presentation.search.viewModel

import com.example.playlistmaker.domain.models.Track

sealed class SearchScreenState {
    object Loading : SearchScreenState()
    data class SearchResults(val data: List<Track>?) : SearchScreenState()
    data class History(val history: List<Track>?) : SearchScreenState()
    data class ErrorNotFound(val message: String?) : SearchScreenState()
    data class ErrorNoEnternet(val message: String?) : SearchScreenState()
}



