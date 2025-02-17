package com.example.tagplayer.home.data

import androidx.media3.common.util.UnstableApi
import com.example.tagplayer.core.data.database.MediaDatabase
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.SharedPrefs
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.home.domain.ScanSongsForeground
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


interface HomeCacheDatasource : ScanSongsForeground {
    fun library(sortingDataType: SortingDataType): Flow<List<Song>>
    suspend fun filters(): List<Long>
    fun filtered(sortingDataType: SortingDataType): Flow<List<Song>>
    suspend fun croppedRecently(): List<SongLastPlayedCrossRef>

    class Base(
        private val database: MediaDatabase,
        private val foregroundWrapper: ForegroundWrapper,
        private val songFilterPrefs: SharedPrefs.Read<List<Long>>,
        private val handleSort: HandleSort
    ) : HomeCacheDatasource {

        override fun library(sortingDataType: SortingDataType): Flow<List<Song>> =
            sortingDataType.map(handleSort)

        override suspend fun croppedRecently(): List<SongLastPlayedCrossRef> =
            database.lastPlayed.croppedRecently()

        override suspend fun filters(): List<Long> {
            return songFilterPrefs.read()
        }

        override fun filtered(sortingDataType: SortingDataType): Flow<List<Song>> =
            sortingDataType.map(handleSort).map { it.toSet() }.map { it.toList() }

        override fun scan() {
            foregroundWrapper.scanMedia()
        }

    }
}