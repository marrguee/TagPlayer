package com.example.tagplayer.search.domain

import com.example.tagplayer.search.presentation.SongSearchUi

data class SongSearchDomain(
    private val id: Long,
    private val title: String,
    private val duration: String
) {
    interface Mapper<T> {
        fun map(id: Long, title: String, duration: String): T

        object ToUi : Mapper<SongSearchUi> {
            override fun map(id: Long, title: String, duration: String): SongSearchUi {
                return SongSearchUi(id, title, duration)
            }
        }
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(id, title, duration)
}
