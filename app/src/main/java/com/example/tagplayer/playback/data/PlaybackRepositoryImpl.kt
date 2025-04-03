package com.example.tagplayer.playback.data

import com.example.tagplayer.core.data.HandleTry
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.playback.domain.PlaybackRepository
import com.example.tagplayer.playback.domain.TagPlaybackDomain
import com.example.tagplayer.playback.domain.errors.PlaybackCustomException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaybackRepositoryImpl(
    private val handleTry: HandleTry<PlaybackCustomException>,
    private val cacheDatasource: PlaybackCacheDatasource,
    private val mapper: SongTag.Mapper<TagPlaybackDomain>
): PlaybackRepository<TagPlaybackDomain, HandleMediaResult> {

    override fun tags(id: Long): Flow<List<TagPlaybackDomain>> = handleTry
        .handle(PlaybackCustomException.FetchTagsException()) {
            cacheDatasource.tags(id).map { list -> list.map { it.map(mapper) } }
        }

    override suspend fun uri(id: Long): String = handleTry
        .handleAsync(PlaybackCustomException.ShareException()) {
            cacheDatasource.uri(id)
        }

    override suspend fun deleteSong(songId: Long): HandleMediaResult = handleTry
        .handleAsync(PlaybackCustomException.DeleteException()) {
            cacheDatasource.deleteSong(songId)
        }
}