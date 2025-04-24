package com.example.playlistmaker.retrofit

data class TrackResponse(
    val resultCount: Int,
    val results: List<Track>
)

data class Track(
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: String, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val trackId: String,
    val collectionName: String, // Добавляем поля в класс трак для показа на экране аудиоплеера
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
)





