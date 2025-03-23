package com.example.tagplayer.core.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tagplayer.core.data.database.SortField
import com.example.tagplayer.home.domain.SongDomain
import com.example.tagplayer.search.domain.SearchDomain

@Entity("songs")
class Song(
    @PrimaryKey
    @ColumnInfo("id") val id: Long,
    @ColumnInfo("image") val image: String?,
    @SortField("title")
    @ColumnInfo("title") val title: String,
    @ColumnInfo("author") val author: String?,
    @ColumnInfo("duration") val duration: Long,
    @ColumnInfo("uri") val uri: String,
    @SortField("data_modified")
    @ColumnInfo("data_modified", defaultValue = "0") val dateModified: Long
) {

    interface Mapper<T> {
        fun map(
            id: Long,
            image: String?,
            title: String,
            author: String?,
            duration: Long,
            dateModified: Long
        ): T

        object ToDomain : Mapper<SongDomain> {
            override fun map(
                id: Long,
                image: String?,
                title: String,
                author: String?,
                duration: Long,
                dateModified: Long
            ) = SongDomain(id, image, title, author, duration)
        }

        object ToDomainSearch : Mapper<SearchDomain> {
            override fun map(
                id: Long,
                image: String?,
                title: String,
                author: String?,
                duration: Long,
                dateModified: Long
            ) = SearchDomain(id, image, title, author, duration)
        }
    }

    fun <T> map(mapper: Mapper<T>): T =
        mapper.map(id, image, title, author, duration, dateModified)

}

