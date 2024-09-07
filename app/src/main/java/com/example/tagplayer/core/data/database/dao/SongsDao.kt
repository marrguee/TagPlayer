package com.example.tagplayer.core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tagplayer.core.data.database.models.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface SongsDao {
    @Query("SELECT * FROM songs")
    fun library() : Flow<List<Song>>
    @Query("SELECT * FROM songs WHERE songs.title LIKE '%' || :query || '%'")
    suspend fun searchSongs(query: String) : List<Song>
    @Insert(entity = Song::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(track: Song) //todo what could be if track was deleted from system
    @Insert(entity = Song::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSongs(songs: List<Song>)
    @Query("SELECT songs.uri FROM songs WHERE songs.id = :songId LIMIT 1")
    suspend fun uriById(songId: Long) : String
}