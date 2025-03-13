package com.example.playlistmaker.retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface ITunesApi {
    @GET("/search?entity=song")
    suspend  fun getSong (@Query("term") text : String,
    @Query("limit") limit: Int = 5) : Response<TrackResponse> // сделал 5 треков пока что
}