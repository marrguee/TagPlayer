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
    @Query("SELECT * FROM tags WHERE id = :id LIMIT 1")
    fun tag(id: Long) : SongTag
    @Query(
        "SELECT tags.id, tags.title, tags.selected, tags.color FROM tags " +
        "INNER JOIN songs_and_tags ON tag_id = id WHERE track_id = :songId"
    )
    fun ownedTags(songId: Long): Flow<List<SongTag>>

    @Insert(entity = SongTag::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTag(tag: SongTag)
    @Query("UPDATE tags SET selected = :selected WHERE id = :id")
    fun updateSelected(id: Long, selected: Boolean)
    @Query("DELETE FROM tags WHERE id = :id")
    suspend fun removeTag(id: Long)

    @Query("UPDATE tags SET selected = 0")
    suspend fun clearSelected()
    @Query(
        "SELECT * FROM tags WHERE tags.id NOT IN " +
        "(SELECT id FROM tags INNER JOIN songs_and_tags sat ON " +
        "id = tag_id WHERE track_id = :songId)"
    )
    fun tagsWithoutOwned(songId: Long): Flow<List<SongTag>>
}