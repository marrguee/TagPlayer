package com.example.tagplayer.core.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.tagplayer.filter_by_tags.domain.TagFilterDomain
import com.example.tagplayer.playback.domain.TagPlaybackDomain
import com.example.tagplayer.tag_settings.domain.TagDomain

@Entity(
    tableName = "tags",
    indices = [
        Index(value = ["title"], unique = true)
    ]
)
data class SongTag(
    @ColumnInfo("title") val title: String,
    @ColumnInfo("color") val color: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Long = 0,
) {
    interface Mapper<T> {
        fun map(id: Long, title: String, color: String) : T

        object ToDomain : Mapper<TagDomain> {
            override fun map(id: Long, title: String, color: String) =
                TagDomain(id, title, color)
        }

        object ToFilterDomain : Mapper<TagFilterDomain> {
            override fun map(id: Long, title: String, color: String) =
                TagFilterDomain(id, title, color)
        }

        object ToPlaybackDomain : Mapper<TagPlaybackDomain> {
            override fun map(id: Long, title: String, color: String) =
                TagPlaybackDomain(title, color)
        }
    }

    fun <T> map(mapper: Mapper<T>) : T = mapper.map(id, title, color)
}