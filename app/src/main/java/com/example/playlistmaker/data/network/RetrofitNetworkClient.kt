package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.NetworkClient
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.dto.TrackSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.jvm.java

class RetrofitNetworkClient : NetworkClient {

    private val iTunesBaseUrl = "https://itunes.apple.com"


    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

   private val iTunesApi = retrofit.create(ITunesApi::class.java)


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