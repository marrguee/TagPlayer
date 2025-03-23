package com.example.tagplayer.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tagplayer.core.data.database.models.LastPlayed
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef

@Dao
interface LastPlayedDao {
    @Query("SELECT * FROM songLastPlayedCrossRef")
    suspend fun recently() : List<SongLastPlayedCrossRef>
    @Query("SELECT * FROM songLastPlayedCrossRef LIMIT :count")
    suspend fun croppedRecently(count: Int) : List<SongLastPlayedCrossRef>
    @Insert(entity = LastPlayed::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun wasPlayed(lastPlayed: LastPlayed)
}