package com.example.tagplayer.core.data

import com.example.tagplayer.home.data.ExtractMedia
import com.example.tagplayer.core.data.database.dao.SongsDao

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