package com.example.tagplayer.search.domain

import com.example.tagplayer.all.domain.DomainError
import com.example.tagplayer.all.domain.HandleError
import com.example.tagplayer.all.domain.SongDomain
import com.example.tagplayer.main.presentation.SongUi
import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.core.data.database.models.SearchHistoryTable
import com.example.tagplayer.search.presentation.SearchUi
import kotlinx.coroutines.flow.map

interface SearchInteractor : UpdateSearchHistory<SearchHistoryTable>, PlaySongForeground {
    suspend fun searchHistory() : SearchResponse
    fun findSongs(query: String) : SearchResponse

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

        override fun findSongs(query: String) = try {
            SearchResponse.SongsSuccess(repository.handleRequest(query).map { list ->
                list.map { it.map(songsModelMapper) }
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