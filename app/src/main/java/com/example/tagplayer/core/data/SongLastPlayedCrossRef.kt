package com.example.tagplayer.core.data

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import androidx.room.TypeConverter
import com.example.tagplayer.history.domain.HistoryDomain
import java.util.Calendar
import java.util.Date

@DatabaseView(viewName = "songLastPlayedCrossRef",
    value = "SELECT songs.id as id, " +
            "songs.title as title, " +
            "songs.duration as duration, " +
            "songs.uri as uri, " +
            "last_played.date as date " +
            "FROM songs INNER JOIN last_played ON songs.id = last_played.song_id " +
            "ORDER BY last_played.date DESC")
class SongLastPlayedCrossRef(
    @Embedded val song: SongData,
    @ColumnInfo("date")
    val date: Date
) {
    interface Mapper<T> {

        fun map(list: List<SongLastPlayedCrossRef>) : List<T>
        fun map(song: SongData) : T

        object ToDomain : Mapper<HistoryDomain> {
            override fun map(list: List<SongLastPlayedCrossRef>): List<HistoryDomain> {
                val result = mutableListOf<HistoryDomain>()
                var lastDate = Date()
                val calendar: Calendar = Calendar.getInstance()
                list.forEach {
                    calendar.time = it.date
                    calendar.set(Calendar.HOUR_OF_DAY, 0)
                    calendar.set(Calendar.MINUTE, 0)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)

                    if (lastDate != calendar.time){
                        lastDate = calendar.time
                        result.add(HistoryDomain.SongDate(lastDate))
                    }
                    result.add(it.map(this))
                }
                return result
            }

            override fun map(song: SongData): HistoryDomain {
                return HistoryDomain.Song(song.id, song.title, song.duration)
            }
        }
    }

    fun <T> map(mapper: Mapper<T>) : T = mapper.map(song)
}

class TimestampConverter {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }
}