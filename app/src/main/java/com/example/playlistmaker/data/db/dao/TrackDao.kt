package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(trackEntity: TrackEntity)

    @Query("DELETE FROM track_table WHERE trackId =:trackId")
    suspend fun deleteTrackForId(trackId: String)


    @Query("SELECT * FROM track_table")
     suspend fun getTracks(): List<TrackEntity>

}