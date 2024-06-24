package com.example.tagplayer.core.data

import com.example.tagplayer.all.data.ExtractMedia

interface MediaStoreHandler {
    suspend fun scan()
    suspend fun uri(id: Long) : String

    class Base(
        private val extractMedia: ExtractMedia,
        private val songsDao: SongsDao
    ) : MediaStoreHandler {
        override suspend fun scan() {
            songsDao.addSongs(extractMedia.media())
        }

        override suspend fun uri(id: Long) = songsDao.uriById(id)
    }
}