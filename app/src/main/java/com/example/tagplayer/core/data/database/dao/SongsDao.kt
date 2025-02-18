package com.example.tagplayer.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.data.database.models.SongTagCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface SongsDao {
    @Query("SELECT * FROM songs")
    fun songs() : List<Song>
    @RawQuery(observedEntities = [Song::class, SongTagCrossRef::class, SongTag::class])
    fun sortedSongs(raw: SimpleSQLiteQuery): Flow<List<Song>>

    @Query("SELECT * FROM songs WHERE title LIKE '%' || TRIM(:query) || '%' COLLATE NOCASE")
    suspend fun search(query: String) : List<Song>

    @Insert(entity = Song::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(track: Song)
    @Insert(entity = Song::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSongs(songs: List<Song>)

    @Query("SELECT uri FROM songs WHERE id = :songId LIMIT 1")
    suspend fun uriById(songId: Long) : String
    @Query("SELECT title FROM songs WHERE id = :songId LIMIT 1")
    suspend fun titleById(songId: Long) : String

    @Delete(entity = Song::class)
    suspend fun deleteSongs(list: List<Song>)
    @Query("DELETE FROM songs WHERE id =:songId")
    suspend fun deleteSong(songId: Long)

    @Query("INSERT INTO songs_and_tags (track_id, tag_id) VALUES (:songId, :tagId)")
    suspend fun attachTag(songId: Long, tagId: Long)
    @Query("DELETE FROM songs_and_tags WHERE track_id = :songId AND tag_id = :tagId")
    suspend fun detachTag(songId: Long, tagId: Long)
}