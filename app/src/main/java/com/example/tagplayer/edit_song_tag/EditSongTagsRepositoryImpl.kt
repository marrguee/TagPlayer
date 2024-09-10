package com.example.tagplayer.edit_song_tag

class EditSongTagsRepositoryImpl(
    private val cacheDatasource: EditSongTagsCacheDatasource
) : EditSongTagsRepository<TagDomain> {

    override suspend fun allTags(): List<TagDomain> {
        return cacheDatasource.allTags().map { TagDomain(it.id, it.title, it.color) }
    }

    override suspend fun ownedTags(songId: Long): List<TagDomain> {
        return cacheDatasource.ownedTags(songId).map { TagDomain(it.id, it.title, it.color) }
    }

    override suspend fun saveOwnedTags(songId: Long, ownedTags: List<TagDomain>) {
        cacheDatasource.saveOwnedTags(songId, ownedTags.map { it.map(songId) })
    }
}