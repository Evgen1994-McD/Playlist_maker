package com.example.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale



object TimeUtils {


    fun parseTrackTime(trackTimeStr: String): Long {
        val parts = trackTimeStr.split(":") // Разбиваем строку на части по ":"

        if (parts.size != 2) throw IllegalArgumentException("Invalid track time format: $trackTimeStr")

        val minutes = parts[0].trim().toLongOrNull() ?: throw NumberFormatException("Invalid minute value: ${parts[0]}")
        val seconds = parts[1].trim().toLongOrNull() ?: throw NumberFormatException("Invalid second value: ${parts[1]}")

        // Переводим минуты и секунды в миллисекунды
        return ( (minutes * 60 * 1000) + (seconds * 1000))

    }

    fun finalTracksTime(time: Long): String{
        return (time/1000/60).toString()
    }
}


