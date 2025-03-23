package com.example.tagplayer.home.data

import com.example.tagplayer.core.data.database.dao.LastPlayedDao
import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.home.domain.HomeRecently
import com.example.tagplayer.home.domain.ProvideGenerateSql
import kotlinx.coroutines.flow.Flow

interface HomeCacheDatasource : HomeRecently<SongLastPlayedCrossRef> {
    fun sorted(field: ObtainFieldName, sql: ProvideGenerateSql): Flow<List<Song>>

    class Base(
        private val lastPlayed: LastPlayedDao,
        private val songsDao: SongsDao,
        private val recentlyCount: Int = 6,
    ) : HomeCacheDatasource {

        override suspend fun croppedRecently(): List<SongLastPlayedCrossRef> =
            lastPlayed.croppedRecently(recentlyCount)

        override fun sorted(field: ObtainFieldName, sql: ProvideGenerateSql): Flow<List<Song>> =
            songsDao.sortedSongs(sql.generateSql().orderBy(field))
    }
}