package com.example.playlistmaker.data.search.dto

data class TrackDto(
    val trackId: String,

    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: String?, // Может быть null от API
    val artworkUrl100: String?, // Может быть null от API
    val collectionName: String?, // Может быть null от API
    val releaseDate: String?, // Может быть null от API
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?, // Может быть null от API
    val isLike: Boolean
)
