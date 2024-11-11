package com.example.tagplayer.home.presentation

import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.home.domain.HomeInteractor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

interface TagFilterMapper {
    fun mapEmptyList()
    fun mapFilledList(list: List<Long>)
    fun mapError(error: String)

    class Mapper(
        private val interactor: HomeInteractor,
        private val observable: CustomObservable.UpdateUi<HomeState>,
        private val songsResponseMapper: SongsResponse.SongsResponseMapper
    ) : TagFilterMapper {
        private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        override fun mapEmptyList() {
            scope.launch {
                interactor.libraryFlow().map(songsResponseMapper)
            }
        }

        override fun mapFilledList(list: List<Long>) {
            scope.launch {
                interactor.filtered(list).map(songsResponseMapper)
            }
        }

        override fun mapError(error: String) {
            observable.update(HomeState.Error(error))
        }
    }
}