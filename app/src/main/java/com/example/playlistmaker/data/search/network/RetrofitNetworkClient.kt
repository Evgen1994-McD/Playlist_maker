package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.TrackResponse
import com.example.playlistmaker.data.search.dto.TrackSearchRequest


class RetrofitNetworkClient(private val iTunesApi: ITunesApi) : NetworkClient {


    override fun doRequest(dto: Any): Response {
        if (dto is TrackSearchRequest) {
            val response = iTunesApi.getSong(dto.expression).execute()

            // Получаем тело ответа
            val body = response.body()?.let { it as TrackResponse } ?: Response()

            // Заполняем resultCode статусом HTTP-запроса
            return body.apply { resultCode = response.code() }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }
}