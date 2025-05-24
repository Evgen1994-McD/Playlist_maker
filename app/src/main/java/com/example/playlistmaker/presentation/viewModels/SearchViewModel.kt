package com.example.playlistmaker.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.Creator
import com.example.playlistmaker.domain.api.FavoriteTrackInteractor
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.models.SearchScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(private val trackInteractor: TrackInteractor,
    private val favoriteTrackInteractor: FavoriteTrackInteractor): ViewModel() {


    private val mutableScreenState = MutableLiveData(SearchScreenState())
    val getLiveData: LiveData<SearchScreenState> get() = mutableScreenState


    fun addTrackToFavorite(track: Track){
        if (trackInteractor.clickDebounce()) {
            favoriteTrackInteractor.addTrack(track)
//            mutableScreenState.value = mutableScreenState.value!!.copy(history = favoriteTrackInteractor.getAllTracksFromStorage())
        }
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
          trackInteractor.searchTracks(
            txtForSearch,
            object : TrackInteractor.TracksConsumer {
                override fun consume(tracks: List<Track>) {
                    mutableScreenState.postValue(mutableScreenState.value!!.copy(isLoading = false, searchResults = tracks))

                }

                override fun onFailure(error: Throwable) {
                    mutableScreenState.postValue(mutableScreenState.value!!.copy(errorMessage = error.toString(), isLoading = false))

                }
            })

        }
    }


    companion object{

        fun getViewModelFactory() : ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return SearchViewModel(
                        Creator.provideTracksInteractor(),
                        Creator.provideFavoriteInteractor()

                    ) as T
                }

            }
    }





}