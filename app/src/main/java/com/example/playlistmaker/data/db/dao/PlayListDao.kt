package com.example.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.models.PlayList

@Dao
interface PlayListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlayList(playListEntity: PlayListEntity): Long


    @Query("DELETE FROM playlist_table WHERE listId =:listId")
    suspend fun deletePlayListForId(listId: Int)

    @Query("SELECT * FROM playlist_table WHERE listId <>:listId")
    suspend fun selectDontDeletedPlaylists(listId: Int) : List<PlayListEntity>

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlayList(): List<PlayListEntity>

    @Query("SELECT * FROM playlist_table WHERE listId =:listId")
    suspend fun getPlaylistById(listId: Int):PlayList


}