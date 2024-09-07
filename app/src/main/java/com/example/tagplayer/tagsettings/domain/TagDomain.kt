package com.example.tagplayer.tagsettings.domain

import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.tagsettings.presentation.TagSettingsUi

class TagDomain(
    private val id: Long,
    private val title: String,
    private val color: String
) {
    interface Mapper<T> {
        fun map(id: Long, title: String, color: String) : T

        object ToUi : Mapper<TagSettingsUi> {
            override fun map(id: Long, title: String, color: String) =
                TagSettingsUi(id, title, color)
        }

        object ToData : Mapper<SongTag> {
            override fun map(id: Long, title: String, color: String) =
                SongTag(title, color)
        }
    }

    fun <T> map(mapper: Mapper<T>) : T = mapper.map(id, title, color)
}