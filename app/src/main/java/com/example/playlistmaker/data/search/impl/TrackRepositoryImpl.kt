package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.dto.TrackResponse
import com.example.playlistmaker.data.search.dto.TrackSearchRequest
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTracks(expression: String):Flow<List<Track>?> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                with(response as TrackResponse) {
val data = results.map{ it ->
    Track(
        it.trackId,
        it.trackName,
        it.artistName,
        formatMillisecondsAsMinSec(it.trackTimeMillis.toLong()), // преобразую и пеоедам время сразу
        getCoverArtwork(it.artworkUrl100).toString(),
        it.collectionName,
        formattedYear(it.releaseDate),
        it.primaryGenreName,
        it.country,
        it.previewUrl,
        it.isLike
    )
}
                    emit(data)


                }
            }
            400 -> {
                emit(emptyList())
            }
            else -> emit(null)
            /*
            эмичу эмпти лист чтобы отработать ошибку отсутствия интернета
             */
        }
    }

    fun formatMillisecondsAsMinSec(milliseconds: Long): String { // функция перевода времени
        val localTime = LocalTime.ofNanoOfDay(milliseconds * 1_000_000)
        val formatter = DateTimeFormatter.ofPattern("mm:ss")
        return localTime.format(formatter)
    }

    fun formattedYear(date: String): String {
        val formatter = DateTimeFormatter.ISO_DATE_TIME
        val localDateTime = LocalDateTime.parse(date, formatter)
        val year = localDateTime.year
        return year.toString()
    }

    fun getCoverArtwork(artworkUrl100: String) =
        artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")


}