package com.example.tagplayer.core.data

import android.net.Uri
import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.home.data.ExtractMedia

interface MediaStoreHandler {
    suspend fun scan()
    suspend fun scanNewFile(uri: Uri)

    class Base(
        private val extractMedia: ExtractMedia,
        private val songsDao: SongsDao
    ) : MediaStoreHandler {

        override suspend fun scan() {
            if (!extractMedia.mediaStoreChanged()) return
            val currentList = extractMedia.media()
            val savedList = songsDao.songs()
            val deleted = savedList.filterNot { currentList.contains(it) }
            val added = currentList.filterNot { savedList.contains(it) }
            if (deleted.isNotEmpty()) songsDao.deleteSongs(deleted)
            if (added.isNotEmpty()) songsDao.addSongs(added)
        }

        override suspend fun scanNewFile(uri: Uri) {
            val song = extractMedia.scanNewFile(uri)
            song?.let { songsDao.addSong(it) }
        }
    }
}