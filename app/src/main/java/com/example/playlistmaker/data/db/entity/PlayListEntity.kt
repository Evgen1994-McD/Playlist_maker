package com.example.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity ( tableName = "playlist_table")
data class PlayListEntity(
    @PrimaryKey(autoGenerate = true)
    val listId: Int?,
    val name: String,
    val about: String,
    val image: Int?,
    val tracksId: String,
    val size: Int

)
