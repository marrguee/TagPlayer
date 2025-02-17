package com.example.tagplayer.recently.domain

import com.example.tagplayer.recently.presentation.RecentlyUi
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


interface RecentlyDomain {
    fun map(): RecentlyUi

    data class Song(
        private val id: Long,
        private val thumbnail: String?,
        private val title: String,
        private val author: String?,
        private val duration: Long
    ) : RecentlyDomain {
        interface Mapper<T> {
            fun map(
                id: Long,
                thumbnail: String?,
                title: String,
                author: String?,
                duration: Long
            ): T

            object ToUi : Mapper<RecentlyUi> {
                override fun map(
                    id: Long,
                    thumbnail: String?,
                    title: String,
                    author: String?,
                    duration: Long
                ): RecentlyUi {
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
                    val formattedDuration =
                        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                    return RecentlyUi(
                        id,
                        thumbnail,
                        title,
                        author ?: String(),
                        formattedDuration
                    )
                }
            }
        }

        override fun map(): RecentlyUi = Mapper.ToUi.map(id, thumbnail, title, author, duration)
    }
}