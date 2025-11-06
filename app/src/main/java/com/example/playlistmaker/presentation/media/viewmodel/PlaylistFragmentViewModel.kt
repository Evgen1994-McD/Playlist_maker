package com.example.playlistmaker.presentation.media.viewmodel

import androidx.compose.runtime.Immutable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

@Immutable
data class PlayListItemUi(
    val playlist: PlayList,
    val key: Any,
    val contentType: String
)

class PlaylistFragmentViewModel(private val playlistInteractor: PlaylistInteractor):ViewModel() {

    private val allPlayListsData = MutableLiveData<ImmutableList<PlayListItemUi>>()

    val getLiveData : LiveData<ImmutableList<PlayListItemUi>> get() = allPlayListsData


     fun getAllPlaylists() = viewModelScope.launch{
        val playlists = playlistInteractor.getAllPlayList()
        allPlayListsData.value = playlists.mapIndexed { index, playlist ->
            PlayListItemUi(
                playlist = playlist,
                key = playlist.listId ?: index,
                contentType = "playList"
            )
        }.toImmutableList()
    }



}