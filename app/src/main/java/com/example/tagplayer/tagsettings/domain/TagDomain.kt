package com.example.tagplayer.tagsettings.domain

import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.tagsettings.data.TagData
import com.example.tagplayer.tagsettings.presentation.TagUi

class TagDomain(
    private val id: Long,
    private val title: String,
    private val color: String
) {
    interface Mapper<T> {
        fun map(id: Long, title: String, color: String) : T

        object ToUi : Mapper<TagUi> {
            override fun map(id: Long, title: String, color: String) =
                TagUi(title, color)
        }

        object ToData : Mapper<SongTag> {
            override fun map(id: Long, title: String, color: String) =
                SongTag(id, title, color)
        }
    }

    fun <T> map(mapper: Mapper<T>) : T = mapper.map(id, title, color)
}