package com.example.tagplayer.edit_song_tag

import com.example.tagplayer.core.data.database.models.SongTagCrossRef

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
