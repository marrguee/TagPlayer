package com.example.tagplayer.search.domain

import com.example.tagplayer.all.domain.DomainError
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.all.domain.SongDomain
import com.example.tagplayer.all.presentation.SongUi
import com.example.tagplayer.core.data.LastPlayed
import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.core.domain.UpdateSongHistory
import com.example.tagplayer.search.data.SearchHistory
import com.example.tagplayer.search.presentation.SearchUi

interface SearchInteractor : UpdateHistory<SearchHistory>, PlaySongForeground, UpdateSongHistory {
    suspend fun searchHistory() : SearchResponse
    suspend fun findSongs(query: String) : SearchResponse

    class Base(
        private val repository: SearchRepository<SearchDomain, SearchHistory>,
        private val handleError: HandleError.Presentation,
        private val searchModelMapper: SearchDomain.Mapper<SearchUi>,
        private val songsModelMapper: SongDomain.Mapper<SongUi>
    ) : SearchInteractor {
        override suspend fun searchHistory() = try {
            SearchResponse.SearchSuccess(repository.searchHistory().map { it.map(searchModelMapper) })
        } catch (e: DomainError) {
            SearchResponse.Error(handleError.handle(e))
        }

        override suspend fun findSongs(query: String) = try {
            SearchResponse.SongsSuccess(repository.findSongs(query).map {
                it.map(songsModelMapper)
            })
        } catch (e: DomainError) {
            SearchResponse.Error(handleError.handle(e))
        }

        override suspend fun updateSearch(searchHistory: SearchHistory) {
            repository.updateSearch(searchHistory)
        }

        override fun playSongForeground(id: Long) {
            repository.playSongForeground(id)
        }

        override suspend fun songToHistory(lastPlayed: LastPlayed) {
            repository.songToHistory(lastPlayed)
        }
    }
}