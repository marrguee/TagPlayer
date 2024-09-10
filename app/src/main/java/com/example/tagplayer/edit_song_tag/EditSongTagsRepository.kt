package com.example.tagplayer.edit_song_tag

interface EditSongTagsRepository<T> {
    suspend fun allTags() : List<T>
    suspend fun ownedTags(songId: Long) : List<T>
    suspend fun saveOwnedTags(songId: Long, ownedTags: List<T>)
}