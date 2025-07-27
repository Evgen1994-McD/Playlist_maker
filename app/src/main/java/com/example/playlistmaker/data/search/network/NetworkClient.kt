package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response
}