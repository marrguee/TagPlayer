package com.example.tagplayer.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tagplayer.core.data.database.models.SongTag
import kotlinx.coroutines.flow.Flow

@Dao
interface TagsDao {
    @Query("SELECT * FROM tags")
    fun tags() : Flow<List<SongTag>>
    @Query("SELECT * FROM tags")
    suspend fun tagsList() : List<SongTag>
    @Query("SELECT * FROM tags LEFT JOIN songs_and_tags " +
            "ON songs_and_tags.tag_id = tags.id " +
            "WHERE songs_and_tags.track_id = :songId")
    fun tagsBySongId(songId: Long): Flow<List<SongTag>>
    @Insert(entity = SongTag::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTag(tag: SongTag)
    @Query("DELETE FROM tags WHERE tags.id = :id")
    suspend fun removeTag(id: Long)
    @Query("SELECT tags.id FROM tags WHERE tags.title = :title LIMIT 1")
    suspend fun idByTitle(title: String) : Long
}