package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlayListTracksEntity
import com.example.playlistmaker.data.db.entity.TrackEntity

@Dao
interface PlayListTracksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTracks(playListTracksEntity: PlayListTracksEntity)

    @Query("DELETE FROM playlist_track_entity WHERE trackId =:trackId")
    suspend fun deleteTrackForId(trackId: String)

    @Query("SELECT * FROM playlist_track_entity WHERE trackId =:trackId")
    suspend fun selectTrackForId(trackId: String) : List<PlayListTracksEntity>

}