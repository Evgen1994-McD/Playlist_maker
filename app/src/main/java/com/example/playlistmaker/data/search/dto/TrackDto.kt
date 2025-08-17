package com.example.playlistmaker.data.search.dto

data class TrackDto(
    val trackId: String,

    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: String, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, // Добавляем поля в класс трак для показа на экране аудиоплеера
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val isLike: Boolean
)
