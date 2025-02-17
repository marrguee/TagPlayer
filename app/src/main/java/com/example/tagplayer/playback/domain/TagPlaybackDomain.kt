package com.example.tagplayer.playback.domain

import com.example.tagplayer.playback.presentation.TagPlaybackUi

data class TagPlaybackDomain(
    private val title: String,
    private val color: String,
) {
    fun <T> map(mapper: Mapper<T>) = mapper.map(title, color)

    interface Mapper<T> {
        fun map(title: String, color: String): T

        object ToUi: Mapper<TagPlaybackUi> {
            override fun map(title: String, color: String): TagPlaybackUi =
                TagPlaybackUi(title, color)
        }
    }
}