package com.example.tagplayer.core.data.database.models

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import com.example.tagplayer.home.domain.SongDomain
import com.example.tagplayer.recently.domain.RecentlyDomain

@DatabaseView(
    viewName = "songLastPlayedCrossRef",
    value = "SELECT songs.id as id, " +
            "songs.image as image, " +
            "songs.title as title, " +
            "songs.author as author, " +
            "songs.duration as duration " +
            "FROM songs LEFT JOIN last_played ON songs.id = last_played.song_id " +
            "ORDER BY last_played.date DESC"
)
class SongLastPlayedCrossRef(
    @ColumnInfo("id") val id: Long,
    @ColumnInfo("image") val image: String?,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("author") val author: String?,
    @ColumnInfo("duration") val duration: Long,
) {
    interface Mapper<T> {
        fun map(list: List<SongLastPlayedCrossRef>): List<T>

        object ToDomainRecently : Mapper<RecentlyDomain> {
            override fun map(list: List<SongLastPlayedCrossRef>): List<RecentlyDomain> {
                val result = mutableListOf<RecentlyDomain>()
                list.forEach {
                    result.add(
                        RecentlyDomain.Song(
                            it.id,
                            it.image,
                            it.title,
                            it.author,
                            it.duration
                        )
                    )
                }
                return result
            }
        }

        object ToDomainHome : Mapper<SongDomain> {
            override fun map(list: List<SongLastPlayedCrossRef>): List<SongDomain> {
                val result = mutableListOf<SongDomain>()
                list.forEach {
                    result.add(
                        SongDomain(
                            it.id,
                            it.image,
                            it.title,
                            it.author,
                            it.duration
                        )
                    )
                }
                return result
            }
        }
    }
}

