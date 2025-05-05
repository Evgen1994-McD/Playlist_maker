package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.Track

class TrackResponse(
    val resultCount: Int,
    val results: List<TrackDto>
) : Response()
