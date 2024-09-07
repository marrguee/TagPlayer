package com.example.tagplayer.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tagplayer.core.data.database.models.LastPlayed
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface LastPlayedDao {
    @Query("SELECT * FROM songLastPlayedCrossRef LIMIT 6")
    suspend fun recently() : List<Song>
    @Query("SELECT * FROM songLastPlayedCrossRef")
    suspend fun fullRecently() : List<SongLastPlayedCrossRef>
    @Insert(entity = LastPlayed::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun wasPlayed(lastPlayed: LastPlayed)
}