package com.example.playlistmaker.domain.models

data class PlayList(
    val listId: Int?,
    val name: String,
    val about: String,
    val image: String?,
    val tracksId: String,
    val size: Int)
