package com.example.tagplayer.home.domain

import com.example.tagplayer.core.domain.PlaySongForeground
import com.example.tagplayer.home.presentation.SongsResponse
import com.example.tagplayer.home.presentation.TagFiltersState
import com.example.tagplayer.main.presentation.SongUi
import kotlinx.coroutines.flow.map

interface HomeInteractor : PlaySongForeground, ScanSongsForeground {
    fun libraryFlow(sortingType: SortingType): SongsResponse
    suspend fun filters(): TagFiltersState
    suspend fun filtered(tags: List<Long>, sortingType: SortingType): SongsResponse
    suspend fun croppedRecently(): SongsResponse

    class Base(
        private val repository: HomeRepository<SongDomain>,
        private val handleError: HandleError<DomainError, String>,
        private val modelMapper: SongDomain.Mapper<SongUi>
    ) : HomeInteractor {

        override fun libraryFlow(sortingType: SortingType) = try {
            SongsResponse.SelectedLibrary(
                repository.library(sortingType).map { list -> list.map { it.map(modelMapper) } }
            )
        } catch (e: DomainError) {
            SongsResponse.Error(handleError.handle(e))
        }

        override suspend fun filters(): TagFiltersState = try {
            val list = repository.filters()
            if (list.isEmpty()) TagFiltersState.EmptyList
            else TagFiltersState.FilledList(list)
        } catch (e: DomainError) {
            TagFiltersState.Error(handleError.handle(e))
        }

        override suspend fun filtered(tags: List<Long>, sortingType: SortingType): SongsResponse =
        try {
            SongsResponse.SelectedLibrary(
                repository.filtered(tags, sortingType).map { list -> list.map { it.map(modelMapper) } }
            )
        } catch (e: DomainError) {
            SongsResponse.Error(handleError.handle(e))
        }

        override suspend fun croppedRecently(): SongsResponse = try {
            SongsResponse.SelectedRecently(
                repository.croppedRecently().map { it.map(modelMapper) }
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