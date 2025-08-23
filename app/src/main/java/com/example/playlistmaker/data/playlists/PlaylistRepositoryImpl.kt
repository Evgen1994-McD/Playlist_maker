package com.example.playlistmaker.data.playlists

import com.example.playlistmaker.data.db.MainDb
import com.example.playlistmaker.data.db.converters.PlayListDbConvertor
import com.example.playlistmaker.data.db.converters.PlaylistTracksDbConvertor
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.data.db.entity.PlayListTracksEntity
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.models.PlayList
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.playlists.PlaylistRepository

class PlaylistRepositoryImpl(private val mainDb: MainDb,
    private val playListDbConvertor: PlayListDbConvertor,
    private val tracksDbConvertor: PlaylistTracksDbConvertor):PlaylistRepository {

    override suspend fun insertTrackInPlaylistTable(track: Track){
        mainDb.playListTracksDao().insertTracks(convertTrackEntityFromTrack(track))
    }

    override suspend fun insertPlayList(playList: PlayList) {
        mainDb.playListDao().insertPlayList(convertEntityFromPlaylist(playList))

    }

    override suspend fun deletePlayListForId(listId:String){
        mainDb.playListDao().deletePlayListForId(listId)
    }


    override suspend fun getAllPlayList():List<PlayList> {
      return  mainDb.playListDao().getAllPlayList().map {
            playListEntity ->
            convertPlaylistFromEntity(playListEntity)
        }
    }


    private fun convertPlaylistFromEntity(playListEntity: PlayListEntity): PlayList{
        return playListDbConvertor.map(playListEntity)
    }

    private fun convertEntityFromPlaylist(playList: PlayList): PlayListEntity{
        return playListDbConvertor.map(playList)
    }

    private fun convertTrackFromTrackEntity(track: PlayListTracksEntity): Track{
        tracksDbConvertor.map(track)

    }

    private fun convertTrackEntityFromTrack(track: Track): PlayListTracksEntity{
        return tracksDbConvertor.map(track)
    }



}