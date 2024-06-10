package com.example.tagplayer.core.data

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tagplayer.all.domain.SongDomain

@Entity("songs")
data class SongData(
    @PrimaryKey
    @ColumnInfo("id") val id: Long,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("duration") val duration: Long,
    @ColumnInfo("uri") val uri: String
) {

    interface Mapper<T> {
        fun map(id: Long, title: String, duration: Long, uri: String) : T

        object ToDomain : Mapper<SongDomain> {
            override fun map(id: Long, title: String, duration: Long, uri: String) =
                SongDomain(id, title, duration, Uri.parse(uri))
        }
    }

    fun <T> map(mapper: Mapper<T>): T =
        mapper.map(id, title, duration, uri)

}