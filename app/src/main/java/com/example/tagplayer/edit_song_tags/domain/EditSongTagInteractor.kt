package com.example.tagplayer.edit_song_tags.domain

import com.example.tagplayer.edit_song_tags.presentation.TagUi

interface EditSongTagInteractor {
    suspend fun allTags(owned: List<Long>) : List<TagUi>
    suspend fun ownedTags(songId: Long) : List<TagUi>
    suspend fun saveOwnedTags(songId: Long, ownedTags: List<TagUi>)

    class Base(
        private val repository: EditSongTagsRepository<TagDomain>,
        private val tagUiMapper: TagUi.Mapper<TagDomain>
    ) : EditSongTagInteractor {
        override suspend fun allTags(owned: List<Long>): List<TagUi> {
            return repository.allTags(owned).map { it.mapToUi() }
        }

        override suspend fun ownedTags(songId: Long): List<TagUi> {
            return repository.ownedTags(songId).map { it.mapToUi() }
        }

        override suspend fun saveOwnedTags(songId: Long, ownedTags: List<TagUi>) {
            repository.saveOwnedTags(songId, ownedTags.map { it.map(tagUiMapper) })
        }
    }
}