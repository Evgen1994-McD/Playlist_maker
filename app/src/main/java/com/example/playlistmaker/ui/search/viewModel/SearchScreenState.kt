package com.example.playlistmaker.ui.search.viewModel

import com.example.playlistmaker.domain.models.Track

sealed class SearchScreenState {
    object Loading : SearchScreenState()
    data class SearchResults(val data: List<Track>?) : SearchScreenState()
    data class History(val history: List<Track>?) : SearchScreenState()
    data class ErrorMessage(val message: String) : SearchScreenState()
}




//    val searchResults: List<Track>? = null,
//    val history: List<Track>? = null,
//    val errorMessage: String? = ""
