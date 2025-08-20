package com.example.playlistmaker.domain.playlists.impl

import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import com.example.playlistmaker.domain.playlists.PlaylistRepository

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository):PlaylistInteractor {
    override suspend fun insertPlayList(playList: PlayList){
        playlistRepository.insertPlayList(playList)
    }

    override suspend fun deletePlayListForId(listId:String){
       playlistRepository.deletePlayListForId(listId)
    }


    override suspend fun getAllPlayList():List<PlayList> {
 return playlistRepository.getAllPlayList()
    }



}