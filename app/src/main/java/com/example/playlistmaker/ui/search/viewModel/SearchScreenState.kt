package com.example.playlistmaker.ui.search.viewModel

import com.example.playlistmaker.domain.models.Track

data class SearchScreenState(
    val isLoading: Boolean = false,
    val searchResults: List<Track>? = null,
    val history: List<Track>? = null,
    val errorMessage: String? = ""
)