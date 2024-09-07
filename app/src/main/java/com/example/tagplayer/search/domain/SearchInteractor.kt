package com.example.tagplayer.search.domain

import com.example.tagplayer.all.domain.DomainError
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.all.domain.SongDomain
import com.example.tagplayer.main.presentation.SongUi
import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.core.data.database.models.SearchHistoryTable
import com.example.tagplayer.search.presentation.SearchUi

interface SearchInteractor : UpdateSearchHistory<SearchHistoryTable>, PlaySongForeground {
    suspend fun searchHistory() : SearchResponse
    suspend fun findSongsByTitle(title: String) : SearchResponse

    class Base(
        private val repository: SearchRepository<SearchDomain, SongDomain>,
        private val handleError: HandleError.Presentation,
        private val searchModelMapper: SearchDomain.Mapper<SearchUi>,
        private val songsModelMapper: SongDomain.Mapper<SongUi>
    ) : SearchInteractor {
        override suspend fun searchHistory() = try {
            SearchResponse.SearchSuccess(repository.searchHistory().map { it.map(searchModelMapper) })
        } catch (e: DomainError) {
            SearchResponse.Error(handleError.handle(e))
        }

        override suspend fun findSongsByTitle(title: String) = try {
            SearchResponse.SongsSuccess(repository.findSongsByTitle(title).map {
                it.map(songsModelMapper)
            })
        } catch (e: DomainError) {
            SearchResponse.Error(handleError.handle(e))
        }

        override suspend fun updateSearch(searchHistoryTable: SearchHistoryTable) {
            repository.updateSearch(searchHistoryTable)
        }

        override fun playSongForeground(id: Long) {
            repository.playSongForeground(id)
        }
    }
}