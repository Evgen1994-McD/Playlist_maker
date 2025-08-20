package com.example.playlistmaker.data.db.converters

import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.models.PlayList

class PlayListDbConvertor {
    fun map(playListEntity: PlayListEntity) : PlayList{
        return PlayList(
            playListEntity.listId,
            playListEntity.name,
            playListEntity.about,
            playListEntity.image,
            playListEntity.tracksId,
            playListEntity.size
        )
    }


    fun map(playList: PlayList) : PlayListEntity{
        return PlayListEntity(
            playList.listId,
            playList.name,
            playList.about,
            playList.image,
            playList.tracksId,
            playList.size
        )
    }


}