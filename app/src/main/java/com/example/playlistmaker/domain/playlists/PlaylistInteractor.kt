package com.example.playlistmaker.domain.playlists

import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.Track

interface PlaylistInteractor {
    suspend fun insertPlayList(playList: PlayList)
    suspend fun deletePlayListForId(listId: String)
    suspend fun getAllPlayList(): List<PlayList>
     suspend fun insertTrackInTrackTable(track: Track)
     suspend fun getTrackOfPlaylistById(ids: String): List<Track>
}