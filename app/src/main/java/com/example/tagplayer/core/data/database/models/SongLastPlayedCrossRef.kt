package com.example.tagplayer.core.data.database.models

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Embedded
import com.example.tagplayer.recently.domain.RecentlyDomain

@DatabaseView(viewName = "songLastPlayedCrossRef",
    value = "SELECT songs.id as id, " +
            "songs.title as title, " +
            "songs.duration as duration " +
            "FROM songs LEFT JOIN last_played ON songs.id = last_played.song_id " +
            "ORDER BY last_played.date DESC")
class SongLastPlayedCrossRef(
    @ColumnInfo("id") val id: Long,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("duration") val duration: Long,
) {
    interface Mapper<T> {
        fun map(list: List<SongLastPlayedCrossRef>) : List<T>

        object ToDomain : Mapper<RecentlyDomain> {
            override fun map(list: List<SongLastPlayedCrossRef>): List<RecentlyDomain> {
                val result = mutableListOf<RecentlyDomain>()
                list.forEach {
                    result.add(RecentlyDomain.Song(it.id, it.title, it.duration))
                }
                return result
            }
        }
    }
}

