package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.models.Track

class TrackDbConvertor {
    fun map(track : Track): TrackEntity {
        return TrackEntity(track.trackId, track.trackName, track.artistName,
            track.trackTimeMillis, track.previewUrl, track.artistName, track.country, track.releaseDate, track.collectionName, track.artworkUrl100)
    }

    fun map(trackEntity : TrackEntity): Track {
        return Track(trackEntity.trackId, trackEntity.trackName, trackEntity.artistName,
            trackEntity.trackTimeMillis, trackEntity.previewUrl, trackEntity.artistName, trackEntity.country, trackEntity.releaseDate, trackEntity.collectionName, trackEntity.artworkUrl100)
    }

}