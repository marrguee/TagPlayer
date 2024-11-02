package com.example.tagplayer.home.domain

import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.main.presentation.SongUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface HomeInteractor : PlaySongForeground, ScanSongsForeground {
    fun libraryFlow(): Flow<List<SongUi>>
    suspend fun recently(): HomeResponse
    suspend fun filters(): List<Long>
    suspend fun filtered(tags: List<Long>): List<SongUi>

    class Base(
        private val repository: HomeRepository<SongDomain>,
        private val handleError: HandleError<DomainError, String>,
        private val modelMapper: SongDomain.Mapper<SongUi>
    ) : HomeInteractor {

        override fun libraryFlow() =
            repository.library().map { list -> list.map { it.map(modelMapper) } }

        override suspend fun recently(): HomeResponse = try {
            HomeResponse.RecentlyHomeResponseSuccess(
                repository.recently().map { it.map(modelMapper) }
            )
        } catch (e: DomainError) {
            HomeResponse.HomeResponseError(handleError.handle(e))
        }

        override suspend fun filters(): List<Long> {
            return repository.filters()
        }

        override suspend fun filtered(tags: List<Long>): List<SongUi> {
            return repository.filtered(tags).map { it.map(modelMapper) }
        }

        override fun playSongForeground(id: Long) =
            repository.playSongForeground(id)

        override fun scan() {
            repository.scan()
        }

    }
}