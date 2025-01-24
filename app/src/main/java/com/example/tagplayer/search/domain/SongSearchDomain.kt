package com.example.tagplayer.search.domain

import com.example.tagplayer.search.presentation.SongSearchUi
import java.util.Locale
import java.util.concurrent.TimeUnit

data class SongSearchDomain(
    private val id: Long,
    private val title: String,
    private val duration: Long
) {
    interface Mapper<T> {
        fun map(id: Long, title: String, duration: Long): T

        object ToUi : Mapper<SongSearchUi> {
            override fun map(id: Long, title: String, duration: Long): SongSearchUi {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
                val formattedDuration =
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                return SongSearchUi(id, title, formattedDuration)
            }
        }
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(id, title, duration)
}
