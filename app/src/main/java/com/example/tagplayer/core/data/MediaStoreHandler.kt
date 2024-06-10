package com.example.tagplayer.core.data

import com.example.tagplayer.all.data.ExtractMedia

interface MediaStoreHandler {
    suspend fun scan()

    class Base(
        private val extractMedia: ExtractMedia,
        private val songsDao: SongsDao
    ) : MediaStoreHandler {
        override suspend fun scan() {
            songsDao.addSongs(extractMedia.media())
        }
    }
}