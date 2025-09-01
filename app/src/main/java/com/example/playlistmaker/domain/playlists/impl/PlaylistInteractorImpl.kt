package com.example.playlistmaker.domain.playlists.impl

import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import com.example.playlistmaker.domain.playlists.PlaylistRepository

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository):PlaylistInteractor {


    override suspend fun selectDontDeletedPlaylists(listId:Int):List<PlayList>{
        return playlistRepository.selectDontDeletedPlaylists(listId)
    }
    override suspend fun deleteTrackOfPlaylistById(trackId: String){
        playlistRepository.deleteTrackOfPlaylistById(trackId)
    }

    override suspend fun insertPlayList(playList: PlayList): Long{
       return playlistRepository.insertPlayList(playList)
    }

    override suspend fun insertTrackInTrackTable(track: Track): Long{
        return playlistRepository.insertTrackInPlaylistTable(track)
    }

    override suspend fun getTrackOfPlaylistById(ids: String): List<Track> {
      return playlistRepository.getTracksOfPlaylistById(ids)
    }

    override suspend fun deletePlayListForId(listId: Int) {
       playlistRepository.deletePlayListForId(listId)
    }


    override suspend fun getAllPlayList():List<PlayList> {
 return playlistRepository.getAllPlayList()
    }

    override suspend fun getPlaylistById(listId:Int):PlayList{
        return playlistRepository.getPlaylistById(listId)

    }



}