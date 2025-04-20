package com.example.playlistmaker.retrofit
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response

interface ITunesApi {
    @GET("/search?entity=song")
    fun getSong (@Query("term") text : String)
     : Call<TrackResponse>
}