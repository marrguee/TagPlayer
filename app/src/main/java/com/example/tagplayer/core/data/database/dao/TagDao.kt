package com.example.tagplayer.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tagplayer.core.data.database.models.SongTag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagDao{

    @Query("SELECT * FROM tags")
    fun tags() : Flow<List<SongTag>>
    @Insert(entity = SongTag::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTag(tag: SongTag)
    @Query("DELETE FROM tags WHERE tags.id = :id")
    suspend fun removeTag(id: Long)
    @Query("SELECT tags.id FROM tags WHERE tags.title = :title LIMIT 1")
    suspend fun idByTitle(title: String) : Long
}