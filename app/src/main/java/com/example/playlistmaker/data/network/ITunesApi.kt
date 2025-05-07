package com.example.playlistmaker.data.network
import com.example.playlistmaker.data.dto.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun getSong (@Query("term") text : String)
     : Call<TrackResponse>
}