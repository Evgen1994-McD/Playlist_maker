package com.example.playlistmaker.ui.player.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteInteractor
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.search.FavoriteTrackInteractor
import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import com.example.playlistmaker.services.AudioPlayerControl
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val noAlbum = "No Album"

class PlayerViewModel(
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
    private val trackFromArgs: Track,
    private val myLikedTracksInteractor: FavoriteInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    private var job: Job? = null
    private var audioPlayerControl: AudioPlayerControl? = null
    private val mutablePlaylistLiveData = MutableLiveData<List<PlayList>>()
    val getPlaylistsLiveData: LiveData<List<PlayList>> get() = mutablePlaylistLiveData
    private val mutableMediaScreen = MutableLiveData(
        PlayerScreenState()
    )
    val getLiveData: LiveData<PlayerScreenState> get() = mutableMediaScreen


    private val playerStateData = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerState(): LiveData<PlayerState> = playerStateData


    fun getAllPlaylist() = viewModelScope.launch {
        mutablePlaylistLiveData.value = playlistInteractor.getAllPlayList()
    }


    fun setAudioPlayerControl(audioPlayerControl: AudioPlayerControl) {
        this.audioPlayerControl = audioPlayerControl

        if (job?.isActive != true) {
            job = viewModelScope.launch {
                audioPlayerControl.getPlayerState().collect {
                    playerStateData.postValue(it)
                }
            }
        }
    }


    fun setNotificationVisible(show:Boolean){
   audioPlayerControl?.setShouldShowNotification(show)
    }


    fun onPlayerButtonClicked() {
        if (playerStateData.value is PlayerState.Playing) {
            audioPlayerControl?.pausePlayback()
        } else {
            audioPlayerControl?.startPlayback()
        }
    }

    fun removeAudioPlayerControl() {
        audioPlayerControl = null
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerControl = null
    }


    fun compareTracksIds(playList: PlayList): Boolean {
        val playlistIds = playList.tracksId.split(",")
        val currentTrackId = trackFromArgs.trackId
        return playlistIds.contains(currentTrackId)

    }

    fun insertTrackInPlaylistsTable(playList: PlayList) = viewModelScope.launch {
        playlistInteractor.insertTrackInTrackTable(trackFromArgs)
        val updatedPlaylist = playList.copy(
            tracksId = "${playList.tracksId},${trackFromArgs.trackId}",
            size = playList.size + 1
        )
        val playlistForSave = playlistInteractor.insertPlayList(updatedPlaylist)

        val allPlaylists = playlistInteractor.getAllPlayList()
        mutablePlaylistLiveData.postValue(allPlaylists)

        if (playlistForSave >= 0) {
            mutableMediaScreen.postValue(mutableMediaScreen.value!!.copy(isSuccess = true))
            delay(50)
            mutableMediaScreen.postValue(mutableMediaScreen.value!!.copy(isSuccess = false))

        } else {
            mutableMediaScreen.postValue(mutableMediaScreen.value!!.copy(isSuccess = false))

        }
    }


    fun intentGetExtraBind() {

        if (!trackFromArgs?.trackName
                .isNullOrEmpty()
        ) {

            controlIsLike(trackFromArgs)
            val trackName = trackFromArgs.trackName
            val previewUrl = trackFromArgs.previewUrl

            val trackTimeMillis = trackFromArgs.trackTimeMillis
            val artistName = trackFromArgs.artistName
            val primaryGenreName = trackFromArgs.primaryGenreName
            val country = trackFromArgs.country
            val relieseDate = trackFromArgs.releaseDate
            val artworkUrl100 = trackFromArgs.artworkUrl100
            val trackId = trackFromArgs.trackId

            if (trackFromArgs.collectionName
                    ?.isNullOrEmpty() == true || trackFromArgs.collectionName
                    ?.contains("No Album") == true // Если нет альбома или ответ сервера содержит No Album то убираем поле с альбомом
            ) {
                mutableMediaScreen.value = mutableMediaScreen.value!!.copy(collectionName = noAlbum)


            } else {
                val collectionName = trackFromArgs.collectionName
                // убираем поле альбом если нет альбома
                mutableMediaScreen.value =
                    mutableMediaScreen.value!!.copy(collectionName = collectionName)
                Log.d("Mylog", previewUrl)

                mutableMediaScreen.value = mutableMediaScreen.value!!.copy(
                    trackName = trackName,
                    primaryGenreName = primaryGenreName,
                    artistName = artistName,
                    trackTimeMillis = trackTimeMillis,
                    artworkUrl100 = artworkUrl100,
                    releaseDate = relieseDate,
                    country = country,
                    trackId = trackId
                )


            }
        } else if (!favoriteTrackInteractor.getAllTracksFromStorage().isNullOrEmpty()) {
            loadLastLikedTrack()
        } else return
    }


    fun loadLastLikedTrack() {// убираем поле альбом если нет альбома
        val myTracks = favoriteTrackInteractor.getAllTracksFromStorage()
        if (!myTracks.isNullOrEmpty()) {
            val track = myTracks[0] // если myTracks не пуст, возьмем свежий трек для плеера
            controlIsLike(track)
            val trackName = track.trackName// убираем поле альбом если нет альбома
            if (track.collectionName?.isNullOrEmpty() == true || track.collectionName?.contains("No Album") == true) {
                mutableMediaScreen.value = mutableMediaScreen.value!!.copy(collectionName = noAlbum)
            } else {
                val collectionName =
                    track.collectionName.toString() // убираем поле альбом если нет альбома
                mutableMediaScreen.value =
                    mutableMediaScreen.value!!.copy(collectionName = collectionName)
            }

            val trackTimeMillis = track.trackTimeMillis
            val artistName = track.artistName
            val primaryGenreName = track.primaryGenreName
            val country = track.country
            val relieseDate = track.releaseDate
            val artworkUrl100 = track.artworkUrl100
            val trackId = track.trackId

            mutableMediaScreen.value = mutableMediaScreen.value!!.copy(
                trackName = trackName,
                trackTimeMillis = trackTimeMillis,
                artistName = artistName,
                primaryGenreName = primaryGenreName,
                country = country,
                releaseDate = relieseDate,
                artworkUrl100 = artworkUrl100,
                trackId = trackId
            )

        }
    }

    fun saveTrackToFavorite() = viewModelScope.launch {
        mutableMediaScreen.value = mutableMediaScreen.value!!.copy(isLike = true)

        myLikedTracksInteractor.saveTrackToFavorite(trackFromArgs)

    }

    fun deleteTrackFromFavorite() = viewModelScope.launch {
        myLikedTracksInteractor.deleteTrackFromFavorite(trackFromArgs)
        mutableMediaScreen.value = mutableMediaScreen.value!!.copy(isLike = false)

    }

    fun controlIsLike(track: Track) = viewModelScope.launch {
        if (myLikedTracksInteractor.controlIsLike(track)) {
            mutableMediaScreen.value = mutableMediaScreen.value!!.copy(isLike = true)
        } else {
            mutableMediaScreen.value = mutableMediaScreen.value!!.copy(isLike = false)
        }
    }

}



