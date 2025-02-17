package com.example.tagplayer.home.presentation

import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.home.domain.HomeInteractor
import com.example.tagplayer.home.domain.SortingType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

interface TagFilterMapper {
    fun mapEmptyList(sortingType: SortingType)
    fun mapFilledList(list: List<Long>, sortingType: SortingType)
    fun mapError(error: String)

    class Mapper(
        private val interactor: HomeInteractor,
        private val observable: CustomObservable.UpdateUi<HomeState>,
        private val songsResponseMapper: SongsResponse.SongsResponseMapper
    ) : TagFilterMapper {
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        override fun mapEmptyList(sortingType: SortingType) {
            scope.launch {
                interactor.libraryFlow(sortingType).map(songsResponseMapper)
            }
        }

        override fun mapFilledList(list: List<Long>, sortingType: SortingType) {
            scope.launch {
                interactor.filtered(list, sortingType).map(songsResponseMapper)
            }
        }

        override fun mapError(error: String) {
            observable.update(HomeState.Error(error))
        }
    }
}