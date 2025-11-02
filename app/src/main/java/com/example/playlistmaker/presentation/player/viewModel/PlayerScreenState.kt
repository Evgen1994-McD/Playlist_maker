package com.example.playlistmaker.presentation.player.viewModel

data class PlayerScreenState(val trackName: String ="", // Название композиции
                             val artistName: String="", // Имя исполнителя
                             val trackTimeMillis: String="", // Продолжительность трека
                             val artworkUrl100: String="", // Ссылка на изображение обложки
                             val trackId: String="",
                             val collectionName: String="", // Добавляем поля в класс трак для показа на экране аудиоплеера
                             val releaseDate: String="",
                             val primaryGenreName: String="",
                             val country: String="",
                             val previewUrl: String="",
                             val progress: String = "00:00",
                             val isPlaying: Boolean = false,
                             val isLike: Boolean = false,
    val isSuccess: Boolean = false
){


}