package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.FavoriteTrackInteractor
import com.example.playlistmaker.domain.api.FavoriteTrackRepository
import java.util.concurrent.Executors

class FavoriteTrackInteractorImpl ( private val repository: FavoriteTrackRepository) : FavoriteTrackInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun getAllTracks(consumer: FavoriteTrackInteractor.FavoriteTrackConsumer) {
       executor.execute {
           try {

               consumer.consume(repository.getAllTracks())
           }catch (ex : Exception) {
               consumer.onFailure(ex)
           }
       }
    }
}