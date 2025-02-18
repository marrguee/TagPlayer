package com.example.tagplayer.core.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.tagplayer.filter.domain.FilterDomain
import com.example.tagplayer.playback.domain.TagPlaybackDomain
import com.example.tagplayer.tag_settings.domain.TagSettingsDomain

@Entity(
    tableName = "tags",
    indices = [
        Index(value = ["title"], unique = true)
    ]
)
data class SongTag(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Long,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("color") val color: String,
    @ColumnInfo("selected", defaultValue = "0") val selected: Boolean = false,
) {
    interface Mapper<T> {
        fun map(id: Long, title: String, color: String, selected: Boolean) : T

        object ToDomain : Mapper<TagSettingsDomain> {
            override fun map(id: Long, title: String, color: String, selected: Boolean) =
                TagSettingsDomain(id, title, color)
        }

        object ToFilterDomain : Mapper<FilterDomain> {
            override fun map(id: Long, title: String, color: String, selected: Boolean) =
                FilterDomain(id, title, color, selected)
        }

        object ToPlaybackDomain : Mapper<TagPlaybackDomain> {
            override fun map(id: Long, title: String, color: String, selected: Boolean) =
                TagPlaybackDomain(title, color)
        }
    }

    fun <T> map(mapper: Mapper<T>) : T = mapper.map(id, title, color, selected)
}