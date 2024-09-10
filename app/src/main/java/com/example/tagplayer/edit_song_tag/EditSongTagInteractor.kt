package com.example.tagplayer.edit_song_tag

interface EditSongTagInteractor {
    suspend fun allTags() : List<TagUi>
    suspend fun ownedTags(songId: Long) : List<TagUi>
    suspend fun saveOwnedTags(songId: Long, ownedTags: List<TagUi>)

    class Base(
        private val repository: EditSongTagsRepository<TagDomain>
    ) : EditSongTagInteractor {
        override suspend fun allTags(): List<TagUi> {
            return repository.allTags().map { it.mapToUi() }
        }

        override suspend fun ownedTags(songId: Long): List<TagUi> {
            return repository.ownedTags(songId).map { it.mapToUi() }
        }

        override suspend fun saveOwnedTags(songId: Long, ownedTags: List<TagUi>) {
            repository.saveOwnedTags(songId, ownedTags.map { it.mapToDomain() })
        }
    }
}