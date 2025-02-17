package com.example.tagplayer.playback_control.data

import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.home.data.HandleMediaResult
import com.example.tagplayer.home.domain.DomainError
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.playback_control.domain.PlaybackRepository
import com.example.tagplayer.playback_control.domain.TagPlaybackDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaybackRepositoryImpl(
    private val cacheDatasource: PlaybackCacheDatasource,
    private val handleError: HandleError<Exception, DomainError>,
    private val tagMapper: SongTag.Mapper<TagPlaybackDomain>
): PlaybackRepository<TagPlaybackDomain> {
    override fun tags(songId: Long): Flow<List<TagPlaybackDomain>> =
        cacheDatasource.tags(songId).map { list -> list.map { it.map(tagMapper) } }

    override suspend fun deleteSong(songId: Long): HandleMediaResult = try {
        cacheDatasource.deleteSong(songId)
    } catch (e: Exception) {
        throw handleError.handle(e)
    }
}