package com.example.tagplayer.edit_song_tags.data

import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.data.database.models.SongTagCrossRef

interface EditSongTagsCacheDatasource {
    suspend fun allTags(owned: List<Long>) : List<SongTag>
    suspend fun ownedTags(songId: Long) : List<SongTag>
    suspend fun saveOwnedTags(songId: Long, ownedTags: List<SongTagCrossRef>)

    class Base(
        private val tagsDao: TagsDao,
        private val songsDao: SongsDao,
    ) : EditSongTagsCacheDatasource {

        override suspend fun allTags(owned: List<Long>): List<SongTag> =
            tagsDao.tagsWithoutOwnedList(owned)

        override suspend fun ownedTags(songId: Long): List<SongTag> =
            songsDao.songTags(songId)

        override suspend fun saveOwnedTags(songId: Long, ownedTags: List<SongTagCrossRef>) {
            songsDao.deleteSongTags(songId)
            songsDao.updateSongTags(ownedTags)
        }
    }
}