package com.example.tagplayer.history.domain

import com.example.tagplayer.main.presentation.SongUi
import com.example.tagplayer.history.presentation.DateUi
import com.example.tagplayer.main.presentation.ItemUi
import java.util.Date
import java.util.concurrent.TimeUnit


interface HistoryDomain {
    fun map() : ItemUi
    data class Song(
        private val id: Long,
        private val title: String,
        private val duration: Long
    ) : HistoryDomain {
        interface Mapper<T> {
            fun map(id: Long, title: String, duration: Long) : T
            object ToUi : Mapper<ItemUi> {
                override fun map(id: Long, title: String, duration: Long): ItemUi {
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(duration) //todo DRY
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) % 60
                    val formattedDuration = String.format("%02d:%02d", minutes, seconds)
                    return SongUi(id, title, formattedDuration)
                }
            }
        }

        override fun map(): ItemUi = Mapper.ToUi.map(id, title, duration)
    }

    data class SongDate(
        private val songDate: Date
    ) : HistoryDomain {
        interface Mapper<T> {
            fun map(date: Date) : T
            object ToUi : Mapper<ItemUi> {
                override fun map(date: Date): ItemUi {
                    return DateUi(date.toString())
                }
            }
        }

        override fun map(): ItemUi = Mapper.ToUi.map(songDate)
    }
}