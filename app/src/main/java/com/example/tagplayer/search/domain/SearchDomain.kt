package com.example.tagplayer.search.domain

import com.example.tagplayer.search.presentation.SearchUi
import java.util.Locale
import java.util.concurrent.TimeUnit

data class SearchDomain(
    private val id: Long,
    private val thumbnail: String?,
    private val title: String,
    private val author: String?,
    private val duration: Long
) {
    interface Mapper<T> {
        fun map(id: Long, thumbnail: String?, title: String, author: String?, duration: Long): T

        object ToUi : Mapper<SearchUi> {
            override fun map(
                id: Long,
                thumbnail: String?,
                title: String,
                author: String?,
                duration: Long
            ): SearchUi {
                val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
                val formattedDuration =
                    String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                return SearchUi(
                    id,
                    thumbnail,
                    title,
                    author ?: String(),
                    formattedDuration
                )
            }
        }
    }

    fun <T> map(mapper: Mapper<T>): T = mapper.map(id, thumbnail, title, author, duration)
}
