package com.example.tagplayer.core.data

import com.example.tagplayer.home.domain.DomainError
import com.example.tagplayer.home.domain.HandleError

abstract class AbstractSongBasedRepository<I, O, P>(
    private val foregroundWrapper: ForegroundWrapper,
    private val handleError: HandleError<Exception, DomainError>,
) {
/*    fun handleRequest(vararg params: P) : Flow<List<O>> = try {
        cacheDatasourceData(*params).map {
            mapList(it)
        }
    } catch (e: Exception) {
        throw handleError.handle(e)
    }*/

    fun playSongForeground(id: Long) {
        foregroundWrapper.playMedia(id)
    }
//    protected abstract fun cacheDatasourceData(vararg params: P) : Flow<List<I>>
//    protected abstract fun mapList(list: List<I>) : List<O>
}