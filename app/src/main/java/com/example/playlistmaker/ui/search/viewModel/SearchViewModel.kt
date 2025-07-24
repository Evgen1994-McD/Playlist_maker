package com.example.playlistmaker.ui.search.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.search.FavoriteTrackInteractor
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private val trackInteractor: TrackInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor
): ViewModel() {


    private val mutableScreenState = MutableLiveData(SearchScreenState())
    val getLiveData: LiveData<SearchScreenState> get() = mutableScreenState


    fun addTrackToFavorite(track: Track){
            favoriteTrackInteractor.addTrack(track)
    }

    fun getAllTracks() {
        mutableScreenState.value = mutableScreenState.value!!.copy(history = favoriteTrackInteractor.getAllTracksFromStorage())

    }

    fun clearHistory(){
        favoriteTrackInteractor.clearHistory()
        mutableScreenState.value = mutableScreenState.value!!.copy(history = null, searchResults = null)

    }




    fun searchTracks( txtForSearch:String){
        viewModelScope.launch(Dispatchers.IO){
            mutableScreenState.postValue(mutableScreenState.value!!.copy(isLoading = true)) // при начале запроса - выставляем лоадинг в тру
            trackInteractor.searchTracks(txtForSearch)
                .collect{ pair->
if(pair.first.isNullOrEmpty()){
    mutableScreenState.postValue(mutableScreenState.value!!.copy(errorMessage = pair.second, isLoading = false))
                    } else {
    mutableScreenState.postValue(mutableScreenState.value!!.copy(isLoading = false, searchResults = pair.first, errorMessage = null))
                    }
                }


        }
    }







}