package com.example.playlistmaker.domain.playlists

import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.Track

interface PlaylistRepository {
    suspend fun insertPlayList(playList: PlayList)
    suspend fun deletePlayListForId(listId: String)
    suspend fun getAllPlayList(): List<PlayList>
    suspend fun insertTrackInPlaylistTable(track: Track)
    suspend fun getTracksOfPlaylistById(ids: String) : List<Track>
}