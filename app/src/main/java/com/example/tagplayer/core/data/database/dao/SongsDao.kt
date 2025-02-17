package com.example.tagplayer.core.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.data.database.models.SongTagCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface SongsDao {
    @Query(
        "SELECT * FROM songs ORDER BY " +
        "CASE WHEN :asc = 1 THEN songs.title END ASC, "+
        "CASE WHEN :asc = 0 THEN songs.title END DESC"
    )
    fun songsSortByTitle(asc: Boolean) : Flow<List<Song>>
    @Query(
        "SELECT * FROM songs ORDER BY " +
                "CASE WHEN :asc = 1 THEN songs.data_modified END ASC, "+
                "CASE WHEN :asc = 0 THEN songs.data_modified END DESC"
    )
    fun songsSortByDate(asc: Boolean) : Flow<List<Song>>
    @Query("SELECT * FROM songs")
    fun songs() : List<Song>
    @Query(
        "SELECT * FROM songs INNER JOIN songs_and_tags " +
                "ON songs_and_tags.track_id = songs.id " +
                "WHERE songs_and_tags.tag_id IN (:tags) " +
                "GROUP BY songs.id HAVING COUNT(DISTINCT songs_and_tags.tag_id) = :count " +
                "ORDER BY CASE WHEN :asc = 1 THEN songs.title END ASC, "+
                "CASE WHEN :asc = 0 THEN songs.title END DESC"
    )
    fun songsTagsSortByTitle(tags: List<Long>, count: Int, asc: Boolean) : Flow<List<Song>>
    @Query(
        "SELECT * FROM songs INNER JOIN songs_and_tags " +
                "ON songs_and_tags.track_id = songs.id " +
                "WHERE songs_and_tags.tag_id IN (:tags) " +
                "GROUP BY songs.id HAVING COUNT(DISTINCT songs_and_tags.tag_id) = :count " +
                "ORDER BY CASE WHEN :asc = 1 THEN songs.data_modified END ASC, "+
                "CASE WHEN :asc = 0 THEN songs.data_modified END DESC"
    )
    fun songsTagsSortByDate(tags: List<Long>, count: Int, asc: Boolean) : Flow<List<Song>>
    @Query("SELECT * FROM songs WHERE songs.title LIKE '%' || TRIM(:query) || '%' COLLATE NOCASE")
    suspend fun searchSongs(query: String) : List<Song>
    @Insert(entity = Song::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(track: Song)
    @Insert(entity = Song::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSongs(songs: List<Song>)
    @Query("SELECT songs.uri FROM songs WHERE songs.id = :songId LIMIT 1")
    suspend fun uriById(songId: Long) : String
    @Query("SELECT songs.title FROM songs WHERE songs.id = :songId LIMIT 1")
    suspend fun titleById(songId: Long) : String
    @Query("DELETE FROM songs_and_tags WHERE songs_and_tags.track_id=:songId")
    suspend fun deleteSongTags(songId: Long)
    @Delete(entity = Song::class)
    suspend fun deleteSongs(list: List<Song>)
    @Query("DELETE FROM songs WHERE songs.id =:songId")
    suspend fun deleteSong(songId: Long)
    @Insert(entity = SongTagCrossRef::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSongTags(tags: List<SongTagCrossRef>)
    @Query("SELECT * FROM tags INNER JOIN songs_and_tags ON songs_and_tags.tag_id = tags.id " +
            "WHERE songs_and_tags.track_id=:songId")
    suspend fun songTags(songId: Long) : List<SongTag>
}