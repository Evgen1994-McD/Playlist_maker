package com.example.playlistmaker.ui.search.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.search.FavoriteTrackInteractor
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.settings.SwitchThemeUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    private val switchThemeUseCase: SwitchThemeUseCase,
    private val trackInteractor: TrackInteractor,
                      private val favoriteTrackInteractor: FavoriteTrackInteractor
): ViewModel() {


    private val mutableScreenState = MutableLiveData(SearchScreenState())
    val getLiveData: LiveData<SearchScreenState> get() = mutableScreenState


    fun addTrackToFavorite(track: Track){
        if (trackInteractor.clickDebounce()) {
            favoriteTrackInteractor.addTrack(track)
        }
    }

    fun getAllTracks() {
       mutableScreenState.value = mutableScreenState.value!!.copy(history = favoriteTrackInteractor.getAllTracksFromStorage())

    }

    fun clearHistory(){
        favoriteTrackInteractor.clearHistory()
       mutableScreenState.value = mutableScreenState.value!!.copy(history = null, searchResults = null)

    }

    fun controlThemeInOtherWindows(){
        switchThemeUseCase.controlThemeInOtherWindows()
    }



    fun searchTracks( txtForSearch:String){
        viewModelScope.launch(Dispatchers.IO){
            mutableScreenState.postValue(mutableScreenState.value!!.copy(isLoading = true)) // при начале запроса - выставляем лоадинг в тру
          trackInteractor.searchTracks(
            txtForSearch,
            object : TrackInteractor.TracksConsumer {
                override fun consume(tracks: List<Track>) {
                    mutableScreenState.postValue(mutableScreenState.value!!.copy(isLoading = false, searchResults = tracks, errorMessage = null))

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
                        Creator.provideSwitchThemeUseCase(),

                        Creator.provideTracksInteractor(),
                        Creator.provideFavoriteInteractor()

                    ) as T
                }

            }
    }





}