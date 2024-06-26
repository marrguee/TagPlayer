package com.example.tagplayer.history.domain

import com.example.tagplayer.history.presentation.HistoryUi
import java.util.Date
import java.util.concurrent.TimeUnit


abstract class HistoryDomain {
    abstract fun map() : HistoryUi
    data class Song(
        private val id: Long,
        private val title: String,
        private val duration: Long
    ) : HistoryDomain() {
        interface Mapper<T> {
            fun map(id: Long, title: String, duration: Long) : T
            object ToUi : Mapper<HistoryUi> {
                override fun map(id: Long, title: String, duration: Long): HistoryUi {
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) //todo DRY
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
                    val formattedDuration = String.format("%02d:%02d", minutes, seconds)
                    return HistoryUi.Song(id, title, formattedDuration)
                }
            }
        }

        override fun map(): HistoryUi = Mapper.ToUi.map(id, title, duration)
    }

    data class SongDate(
        private val songDate: Date
    ) : HistoryDomain() {
        interface Mapper<T> {
            fun map(date: Date) : T
            object ToUi : Mapper<HistoryUi> {
                override fun map(date: Date): HistoryUi {
                    return HistoryUi.Date(date.toString())
                }
            }
        }

        override fun map(): HistoryUi = Mapper.ToUi.map(songDate)
    }
}