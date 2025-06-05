package com.example.playlistmaker.data.search.dto

class TrackResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()
