package com.example.playlistmaker.utils

import com.example.playlistmaker.domain.models.Track

interface OnTrackClickListener {  // интерфейс который будет отвечать за определение позиции трека
    fun onTrackClicked(track: Track) // С помощью него буду выбирать треки для добавления в коллекцию ( спринт 10)
}