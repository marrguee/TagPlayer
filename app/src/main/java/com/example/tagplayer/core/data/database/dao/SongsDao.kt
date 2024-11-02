package com.example.tagplayer.core.data.database.dao

import android.net.Uri
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
    @Query("SELECT * FROM songs")
    fun library() : Flow<List<Song>>
    @Query("SELECT * FROM songs")
    fun songs() : List<Song>
    @Query("SELECT * FROM songs WHERE songs.title LIKE '%' || :query || '%'")
    suspend fun searchSongs(query: String) : List<Song>
    @Insert(entity = Song::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(track: Song) //todo what could be if track was deleted from system
    @Insert(entity = Song::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSongs(songs: List<Song>)
    @Query("SELECT songs.uri FROM songs WHERE songs.id = :songId LIMIT 1")
    suspend fun uriById(songId: Long) : String
    @Query("DELETE FROM songs_and_tags WHERE songs_and_tags.track_id=:songId")
    suspend fun deleteSongTags(songId: Long)
    @Delete(entity = Song::class)
    suspend fun deleteSongs(list: List<Song>)
    @Query("DELETE FROM songs WHERE songs.uri =:uri")
    suspend fun deleteSong(uri: String)
    @Insert(entity = SongTagCrossRef::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSongTags(tags: List<SongTagCrossRef>)
    @Query("SELECT * FROM tags INNER JOIN songs_and_tags ON songs_and_tags.tag_id = tags.id WHERE songs_and_tags.track_id=:songId")
    suspend fun songTags(songId: Long) : List<SongTag>
    @Query(
        "SELECT * FROM songs INNER JOIN songs_and_tags " +
                "ON songs_and_tags.track_id = songs.id " +
                "WHERE songs_and_tags.tag_id IN (:tags)"
    )
    suspend fun songsByTagsId(tags: List<Long>) : List<Song>
}