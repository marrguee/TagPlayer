package com.example.tagplayer.search.domain

import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.home.domain.DomainError
import com.example.tagplayer.home.domain.HandleError
import com.example.tagplayer.search.presentation.SongSearchUi

interface SearchInteractor : PlaySongForeground {
    suspend fun findSongsByTitle(title: String) : SearchResponse

    class Base(
        private val repository: SearchRepository<SongSearchDomain>,
        private val handleError: HandleError.Presentation,
        private val songsModelMapper: SongSearchDomain.Mapper<SongSearchUi>
    ) : SearchInteractor {

        override suspend fun findSongsByTitle(title: String) = try {
            SearchResponse.SongsSuccess(repository.findSongsByTitle(title).map {
                it.map(songsModelMapper)
            })
        } catch (e: DomainError) {
            SearchResponse.Error(handleError.handle(e))
        }

        override fun playSongForeground(id: Long) {
            repository.playSongForeground(id)
        }
    }
}