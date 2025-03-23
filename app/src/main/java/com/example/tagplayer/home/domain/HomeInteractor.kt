package com.example.tagplayer.home.domain

import com.example.tagplayer.core.domain.HandleResponse
import com.example.tagplayer.core.domain.PlayForeground
import com.example.tagplayer.core.domain.ScanForeground
import com.example.tagplayer.home.presentation.SongUi

interface HomeInteractor : PlayForeground, ScanForeground {
    suspend fun croppedRecently(): SongsResponse
    fun sortedSongs(sortingType: SortType.Map): SongsResponse

    class Base(
        private val repository: HomeRepository<SongDomain>,
        private val handleResponse: HandleResponse.Handle<SongsResponse>,
        private val mapper: SongDomain.Mapper<SongUi>,
        private val sortMapper: SortType.Mapper<SongUi>
    ) : HomeInteractor {

        override suspend fun croppedRecently(): SongsResponse = handleResponse.handleAsync {
            SongsResponse.Recently(repository.croppedRecently().map { it.map(mapper) })
        }

        override fun sortedSongs(sortingType: SortType.Map): SongsResponse = handleResponse
            .handle { SongsResponse.Library(sortingType.map(sortMapper)) }

        override fun play(id: Long) = repository.play(id)

        override fun scan() = repository.scan()
    }
}