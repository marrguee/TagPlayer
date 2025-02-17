package com.example.tagplayer.playback_control.domain

import com.example.tagplayer.home.data.HandleMediaResult
import com.example.tagplayer.home.domain.DomainError
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.playback_control.presentation.TagPlaybackUi
import kotlinx.coroutines.flow.map

interface PlaybackControlInteractor {
    fun tags(songId: Long): TagsResult
    suspend fun deleteSong(songId: Long): HandleSongResult

    class Base(
        private val playbackRepository: PlaybackRepository<TagPlaybackDomain>,
        private val repositoryResultMapper: HandleMediaResult.Mapper,
        private val handleError: HandleError<DomainError, String>,
        private val tagMapper: TagPlaybackDomain.Mapper<TagPlaybackUi>
    ): PlaybackControlInteractor {
        override fun tags(songId: Long): TagsResult = try {
            TagsResult.TagsFlow(
                playbackRepository.tags(songId).map { list -> list.map { it.map(tagMapper) } }
            )
        } catch (e: DomainError) {
            TagsResult.Error(handleError.handle(e))
        }

        override suspend fun deleteSong(songId: Long) = try {
            playbackRepository.deleteSong(songId).map(repositoryResultMapper)
        } catch (e: DomainError) {
            HandleSongResult.Error(handleError.handle(e))
        }
    }
}