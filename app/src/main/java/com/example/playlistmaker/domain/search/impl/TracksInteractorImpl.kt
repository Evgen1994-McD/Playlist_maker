package com.example.playlistmaker.domain.search.impl

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.search.TrackInteractor
import com.example.playlistmaker.domain.search.TrackRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future

class TracksInteractorImpl(
    private val repository: TrackRepository

) : TrackInteractor {


    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {



return repository.searchTracks(expression).map { results->
try {
    Pair(results, null)
}
catch (ex:Exception){
    Pair(null, ex.message)
}


}


    }







}

