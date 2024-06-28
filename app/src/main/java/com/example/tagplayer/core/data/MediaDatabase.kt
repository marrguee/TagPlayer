package com.example.tagplayer.core.data

import androidx.room.AutoMigration
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.DeleteColumn
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import com.example.tagplayer.search.data.SearchHistory
import com.example.tagplayer.search.domain.GetSearchHistory
import com.example.tagplayer.search.domain.UpdateHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface SongsDao{
    @Query("SELECT * FROM songs")
    fun songs() : Flow<List<SongData>>
    @Query("SELECT * FROM songs WHERE songs.title LIKE '%' || :query || '%'")
    suspend fun findSongs(query: String) : List<SongData>
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

@Dao
interface LastPlayedDao {
    @Query("SELECT * FROM songLastPlayedCrossRef")
    fun playedHistory() : Flow<List<SongLastPlayedCrossRef>>

    @Insert(entity = LastPlayed::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun wasPlayed(lastPlayed: LastPlayed)
}

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_history ORDER BY date DESC")
    suspend fun searchHistory() : List<SearchHistory>
    @Insert(entity = SearchHistory::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSearch(searchHistory: SearchHistory)
}
@Database(
    entities = [
        SongData::class,
        SongTag::class,
        SongTagCrossRef::class,
        LastPlayed::class,
        SearchHistory::class,
    ],
    views = [
        SongLastPlayedCrossRef::class,
    ],
    version = 5,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, TwoToThree::class),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
    ]
)
@TypeConverters(TimestampConverter::class)
abstract class MediaDatabase : RoomDatabase() {
    abstract val songsDao: SongsDao
    abstract val tagsDao: TagDao
    abstract val lastPlayed: LastPlayedDao
    abstract val searchHistoryDao: SearchHistoryDao
}

@DeleteColumn(tableName = "last_played", columnName = "time")
class TwoToThree : AutoMigrationSpec
