package com.example.tagplayer.core.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface SongsDao{
    @Query("SELECT * FROM songs")
    fun songs() : Flow<List<SongData>>
    @Insert(entity = SongData::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSong(track: SongData) //todo what could be if track was deleted from system
    @Insert(entity = SongData::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSongs(songs: List<SongData>)
    @Query("SELECT songs.uri FROM songs WHERE songs.id = :songId LIMIT 1")
    suspend fun uriById(songId: Long) : String
}

@Dao
interface TagDao{
    @Insert(entity = SongTag::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTag(tag: SongTag)
    @Delete(entity = SongTag::class)
    suspend fun removeTag(tag: SongTag)
    @Query("SELECT * FROM tags")
    suspend fun tags() : List<SongTag>
    @Query("SELECT tags.id FROM tags WHERE tags.title = :title LIMIT 1")
    suspend fun idByTitle(title: String) : Long
}

@Dao
interface SongsAndTagsDao{
    suspend fun tagsBySongId(songId: Long)
    @Query(
        "SELECT * FROM songs INNER JOIN songs_and_tags " +
        "ON songs_and_tags.track_id = songs.id " +
        "AND songs_and_tags.tag_id IN (:tags)"
    )
    suspend fun songsByTagsId(tags: List<SongTag>) : List<SongData>
}
@Database(entities = [SongData::class, SongTag::class, SongTagCrossRef::class], version = 1)
abstract class MediaDatabase : RoomDatabase() {
    abstract val songsDao: SongsDao
    abstract val tagsDao: TagDao
}