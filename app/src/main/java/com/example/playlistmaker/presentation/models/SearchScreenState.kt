package com.example.playlistmaker.presentation.models

import com.example.playlistmaker.domain.models.Track

data class SearchScreenState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val searchResults: List<Track>? = null,
    val history: List<String> = listOf(),
    val errorMessage: String? = ""
)