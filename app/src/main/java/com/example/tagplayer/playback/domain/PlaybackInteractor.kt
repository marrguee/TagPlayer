package com.example.tagplayer.playback.domain

import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.home.data.HandleMediaResult
import com.example.tagplayer.playback.presentation.TagPlaybackUi
import kotlinx.coroutines.flow.map

interface PlaybackInteractor : HandleSongDetails.Delete<SongDetailsResponse> {
    fun tags(id: Long) : SongDetailsResponse
    suspend fun share(id: Long) : SongDetailsResponse

    class Base(
        private val repository: PlaybackRepository<TagPlaybackDomain, HandleMediaResult>,
        private val handleResponse: HandleResponse.Handle<SongDetailsResponse>,
        private val mapper: HandleMediaResult.Mapper,
        private val tagMapper: TagPlaybackDomain.Mapper<TagPlaybackUi>
    ): PlaybackInteractor {
        override fun tags(id: Long) : SongDetailsResponse = handleResponse.handle {
            SongDetailsResponse.TagsFlow(
                repository.tags(id).map { list -> list.map { it.map(tagMapper) } }
            )
        }

        override suspend fun share(id: Long) : SongDetailsResponse = handleResponse.handleAsync {
            SongDetailsResponse.SongUri(repository.uri(id))
        }

        override suspend fun deleteSong(songId: Long) : SongDetailsResponse = handleResponse
            .handleAsync {
                repository.deleteSong(songId).map(mapper)
            }
    }
}