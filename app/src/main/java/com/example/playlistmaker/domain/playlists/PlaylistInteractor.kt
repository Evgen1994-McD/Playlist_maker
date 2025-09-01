package com.example.playlistmaker.domain.playlists

import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.Track

interface PlaylistInteractor {
    suspend fun deleteTrackOfPlaylistById(trackId: String)
    suspend fun insertPlayList(playList: PlayList): Long
    suspend fun deletePlayListForId(listId: Int)
    suspend fun getAllPlayList(): List<PlayList>
     suspend fun insertTrackInTrackTable(track: Track): Long
    suspend fun getTrackOfPlaylistById(ids: String): List<Track>
    suspend fun getPlaylistById(listId:Int):PlayList
    suspend fun selectDontDeletedPlaylists(listId:Int):List<PlayList>
}