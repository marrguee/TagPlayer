package com.example.tagplayer.core.data.database.models

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import com.example.tagplayer.recently.domain.RecentlyDomain
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
    @Embedded val song: Song,
    @ColumnInfo("date")
    val date: Date
) {
    interface Mapper<T> {
        fun map(list: List<SongLastPlayedCrossRef>) : List<T>

        object ToDomain : Mapper<RecentlyDomain> {
            override fun map(list: List<SongLastPlayedCrossRef>): List<RecentlyDomain> {
                val result = mutableListOf<RecentlyDomain>()
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
                        result.add(RecentlyDomain.SongDate(lastDate))
                    }
                    result.add(RecentlyDomain.Song(it.song.id, it.song.title, it.song.duration))
                }
                return result
            }
        }
    }
}

