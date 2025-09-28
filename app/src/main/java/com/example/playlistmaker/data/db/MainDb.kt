package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.data.db.dao.PlayListDao
import com.example.playlistmaker.data.db.dao.PlayListTracksDao
import com.example.playlistmaker.data.db.dao.TrackDao
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.data.db.entity.PlayListTracksEntity
import com.example.playlistmaker.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class, PlayListEntity::class, PlayListTracksEntity::class])
abstract class  MainDb : RoomDatabase() {

    abstract fun trackDao():TrackDao
    abstract fun playListDao():PlayListDao
    abstract fun playListTracksDao(): PlayListTracksDao

}