package com.example.tagplayer.recently.domain

import com.example.tagplayer.recently.presentation.RecentlyUi
import java.util.Date
import java.util.concurrent.TimeUnit


interface RecentlyDomain {
    fun map() : RecentlyUi

    data class Song(
        private val id: Long,
        private val title: String,
        private val duration: Long
    ) : RecentlyDomain {
        interface Mapper<T> {
            fun map(id: Long, title: String, duration: Long) : T
            object ToUi : Mapper<RecentlyUi> {
                override fun map(id: Long, title: String, duration: Long): RecentlyUi {
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) //todo DRY
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
                    val formattedDuration = String.format("%02d:%02d", minutes, seconds)
                    return RecentlyUi.RecentlySongUi(id, title, formattedDuration)
                }
            }
        }

        override fun map(): RecentlyUi = Mapper.ToUi.map(id, title, duration)
    }

    data class SongDate(
        private val songDate: Date
    ) : RecentlyDomain {

        interface Mapper<T> {
            fun map(date: Date) : T
            object ToUi : Mapper<RecentlyUi> {
                override fun map(date: Date): RecentlyUi {
                    return RecentlyUi.DateUi(date.toString())
                }
            }
        }

        override fun map(): RecentlyUi = Mapper.ToUi.map(songDate)
    }
}