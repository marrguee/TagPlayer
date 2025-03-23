package com.example.tagplayer.tags_attach.domain

import com.example.tagplayer.core.data.database.models.SongTagCrossRef
import com.example.tagplayer.tags_attach.presentation.TagUi

data class TagDomain(
    private val id: Long,
    private val title: String,
    private val color: String
) {
    fun map(songId: Long) : SongTagCrossRef {
        return SongTagCrossRef(songId, id)
    }
    fun <T> map(mapper: Mapper<T>): T = mapper.map(id, title, color)

    interface Mapper<T> {
        fun map(id: Long, title: String, color: String): T

        object Ui : Mapper<TagUi> {
            override fun map(id: Long, title: String, color: String): TagUi =
                TagUi(id, title, color)
        }
    }
}
