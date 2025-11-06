package com.example.playlistmaker.presentation.search.viewModel

import androidx.compose.runtime.Immutable
import com.example.playlistmaker.domain.models.Track

@Immutable
sealed class SearchScreenState {
    object Loading : SearchScreenState()
    @Immutable
    data class SearchResults(val data: List<Track>) : SearchScreenState()
    @Immutable
    data class History(val history: List<Track>) : SearchScreenState()
    @Immutable
    data class ErrorNotFound(val message: String?) : SearchScreenState()
    @Immutable
    data class ErrorNoEnternet(val message: String?) : SearchScreenState()
}



