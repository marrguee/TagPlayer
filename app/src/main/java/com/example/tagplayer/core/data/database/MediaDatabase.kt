package com.example.tagplayer.core.data.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tagplayer.core.data.database.dao.LastPlayedDao
import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.data.database.dao.TagsDao
import com.example.tagplayer.core.data.database.models.LastPlayed
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.data.database.models.SongLastPlayedCrossRef
import com.example.tagplayer.core.data.database.models.SongTag
import com.example.tagplayer.core.data.database.models.SongTagCrossRef

@Database(
    entities = [
        Song::class,
        SongTag::class,
        SongTagCrossRef::class,
        LastPlayed::class
    ],
    views = [
        SongLastPlayedCrossRef::class,
    ],
    version = 6,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, TwoToThreeMigration::class),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5),
        AutoMigration(from = 5, to = 6, FifeToSixMigration::class),
    ]
)
@TypeConverters(TimestampConverter::class)
abstract class MediaDatabase : RoomDatabase() {
    abstract val songsDao: SongsDao
    abstract val tagsDao: TagsDao
    abstract val lastPlayed: LastPlayedDao
}