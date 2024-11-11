package com.example.tagplayer.home.domain

import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.home.presentation.SongsResponse
import com.example.tagplayer.home.presentation.TagFiltersResponse
import com.example.tagplayer.main.presentation.SongUi
import kotlinx.coroutines.flow.map

interface HomeInteractor : PlaySongForeground, ScanSongsForeground {
    fun libraryFlow(): SongsResponse
    suspend fun filters(): TagFiltersResponse
    suspend fun filtered(tags: List<Long>): SongsResponse

    class Base(
        private val repository: HomeRepository<SongDomain>,
        private val handleError: HandleError<DomainError, String>,
        private val modelMapper: SongDomain.Mapper<SongUi>
    ) : HomeInteractor {

        override fun libraryFlow() = try {
            SongsResponse.SelectedLibrary(
                repository.library().map { list -> list.map { it.map(modelMapper) } }
            )
        } catch (e: DomainError) {
            SongsResponse.Error(handleError.handle(e))
        }

        override suspend fun filters(): TagFiltersResponse = try {
            val list = repository.filters()
            if (list.isEmpty()) TagFiltersResponse.EmptyList
            else TagFiltersResponse.FilledList(list)
        } catch (e: DomainError) {
            TagFiltersResponse.Error(handleError.handle(e))
        }

        override suspend fun filtered(tags: List<Long>): SongsResponse = try {
            SongsResponse.SelectedLibrary(
                repository.filtered(tags).map { list -> list.map { it.map(modelMapper) } }
            )
        } catch (e: DomainError) {
            SongsResponse.Error(handleError.handle(e))
        }

        override fun playSongForeground(id: Long) =
            repository.playSongForeground(id)

        override fun scan() =
            repository.scan()

    }
}