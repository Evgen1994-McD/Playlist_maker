package com.example.playlistmaker.domain.playlists

import com.example.playlistmaker.domain.models.PlayList

interface PlaylistInteractor {
    suspend fun insertPlayList(playList: PlayList)
    suspend fun deletePlayListForId(listId: String)
    suspend fun getAllPlayList(): List<PlayList>
}