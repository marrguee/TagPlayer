package com.example.tagplayer.tag_settings.domain

import com.example.tagplayer.tag_settings.presentation.TagSettingsUi

class TagSettingsDomain(
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
    }

    fun <T> map(mapper: Mapper<T>) : T = mapper.map(id, title, color)
}