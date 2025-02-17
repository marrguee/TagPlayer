package com.example.tagplayer.home.data

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.media3.common.util.UnstableApi
import com.example.tagplayer.core.data.database.dao.SongsDao


interface HandleMediaStore {
    suspend fun scan(context: Context)
    suspend fun scanNewFile(uri: Uri)
    suspend fun deleteSong(songId: Long): HandleMediaResult

    class Base(
        private val extractMedia: ExtractMedia,
        private val extractMapper: ExtractMediaResult.Mapper,
        private val songsDao: SongsDao
    ) : HandleMediaStore {

        override suspend fun scan(context: Context) {
            if (!extractMedia.mediaStoreChanged(context)) return
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

        override suspend fun deleteSong(songId: Long): HandleMediaResult =
            extractMedia.deleteSong(songId).map(extractMapper)
    }
}