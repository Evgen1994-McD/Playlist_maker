package com.example.playlistmaker.presentation.search.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.FavoriteTrackInteractor
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor
): ViewModel() {

    companion object{

        private const val retryStateString = "retry"

    }

    // Добавляем поле для хранения текущего поискового запроса
    private val _currentSearchQuery = MutableLiveData<String>("")
    val currentSearchQuery: LiveData<String> = _currentSearchQuery

    private val mutableScreenState = MutableLiveData<SearchScreenState>()
    val getLiveData: LiveData<SearchScreenState> get() = mutableScreenState



    fun addTrackToFavorite(track: Track){
            favoriteTrackInteractor.addTrack(track)
    }

    fun getAllTracks() {
            mutableScreenState.value = SearchScreenState.History(history = favoriteTrackInteractor.getAllTracksFromStorage())

    }



    fun clearSearchHistory(){
//        mutableScreenState.postValue(SearchScreenState.SearchResults(null))
        mutableScreenState.postValue(SearchScreenState.ErrorNotFound(retryStateString))
    }


    fun clearHistory(){
        favoriteTrackInteractor.clearHistory()
//        mutableScreenState.postValue(SearchScreenState.History(null))

    }

    // Метод для обновления поискового запроса без выполнения поиска
    fun updateSearchQuery(query: String) {
        _currentSearchQuery.value = query
    }




    fun searchTracks( txtForSearch:String){
        // Сохраняем текущий запрос
        _currentSearchQuery.value = txtForSearch
        viewModelScope.launch(Dispatchers.IO){
            mutableScreenState.postValue(SearchScreenState.Loading) // при начале запроса - выставляем лоадинг в тру
            trackInteractor.searchTracks(txtForSearch)
                .collect{ pair->
if(pair.first==null && pair.second == "Exception" ){
    mutableScreenState.postValue(SearchScreenState.ErrorNoEnternet(pair.second.toString()))
                    }
                    if(pair.first.isNullOrEmpty() && pair.second==null){
                        mutableScreenState.postValue(SearchScreenState.ErrorNotFound(null))
                    }



else if (!pair.first.isNullOrEmpty()) {
    mutableScreenState.postValue(SearchScreenState.SearchResults(pair.first!!))
                    }
                }


        }
    }







}