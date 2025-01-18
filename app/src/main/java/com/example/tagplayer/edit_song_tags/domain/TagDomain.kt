package com.example.tagplayer.edit_song_tags.domain

import com.example.tagplayer.core.data.database.models.SongTagCrossRef
import com.example.tagplayer.edit_song_tags.presentation.TagUi

data class TagDomain(
    private val id: Long,
    private val title: String,
    private val color: String
) {
    fun map(songId: Long) : SongTagCrossRef {
        return SongTagCrossRef(songId, id)
    }
    fun mapToUi() : TagUi = TagUi(id, title, color)
}
