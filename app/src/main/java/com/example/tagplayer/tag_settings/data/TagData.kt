package com.example.tagplayer.tag_settings.data

import com.example.tagplayer.tag_settings.domain.TagDomain

data class TagData(
    private val id: Long,
    private val title: String,
    private val color: String
) {
    interface Mapper<T> {
        fun map(id: Long, title: String, color: String) : T

        object Base : Mapper<TagDomain> {
            override fun map(id: Long, title: String, color: String) =
                TagDomain(id, title, color)
        }
    }

    fun <T> map(mapper: Mapper<T>) : T = mapper.map(id, title, color)
}