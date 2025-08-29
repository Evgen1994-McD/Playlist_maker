package com.example.playlistmaker.data.playlists

import android.util.Log
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
    private val playlistTracksDbConvertor: PlaylistTracksDbConvertor):PlaylistRepository {

    override suspend fun insertTrackInPlaylistTable(track: Track): Long{
       return mainDb.playListTracksDao().insertTracks(convertTrackEntityFromTrack(track))
    }
    override suspend fun getTracksOfPlaylistById(ids: String) : List<Track>{
        val soloTrackId = ids.split(",")

        val tempTrackEntityList = ArrayList<PlayListTracksEntity>()

            soloTrackId.forEach { trackId ->
               val tempTrackEntity = mainDb.playListTracksDao().getTrackById(trackId)
                if (tempTrackEntity != null) {
                    tempTrackEntityList.add((tempTrackEntity))
                }
        }

            return tempTrackEntityList.map {
                    trackEntity ->
                playlistTracksDbConvertor.map(trackEntity)
        }

    }

    override suspend fun insertPlayList(playList: PlayList): Long{
       return mainDb.playListDao().insertPlayList(convertEntityFromPlaylist(playList))

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
       return playlistTracksDbConvertor.map(track)

    }

    private fun convertTrackEntityFromTrack(track: Track): PlayListTracksEntity{
        return playlistTracksDbConvertor.map(track)
    }



}