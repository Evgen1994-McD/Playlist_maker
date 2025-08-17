package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.models.Track

class TrackDbConvertor {
    fun map(track: Track): TrackEntity {

        return TrackEntity(
            trackId = track.trackId,

            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl,
            isLike = track.isLike
        )
    }

    fun map(trackEntity: TrackEntity): Track {
        return Track(
            trackId = trackEntity.trackId,
           trackName =  trackEntity.trackName,
            artistName = trackEntity.artistName,
          trackTimeMillis = trackEntity.trackTimeMillis,
            artworkUrl100 = trackEntity.artworkUrl100,
            collectionName = trackEntity.collectionName,
            releaseDate = trackEntity.releaseDate,
            primaryGenreName = trackEntity.primaryGenreName,
            country = trackEntity.country,
            previewUrl = trackEntity.previewUrl,
            isLike = trackEntity.isLike
        )
    }

}