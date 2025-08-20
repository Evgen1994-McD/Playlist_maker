package com.example.playlistmaker.domain.models

data class PlayList(
    val listId: Int?,
    val name: String,
    val about: String,
    val image: Int?,
    val tracksId: String,
    val size: Int)
