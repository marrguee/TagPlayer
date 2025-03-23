package com.example.tagplayer.core.data.database

import androidx.room.DeleteColumn
import androidx.room.RenameColumn
import androidx.room.migration.AutoMigrationSpec

@RenameColumn.Entries(
    RenameColumn(
        tableName = "songs_and_tags",
        fromColumnName = "track_id",
        toColumnName = "song_id"
    )
)
class ElevenToTwenty : AutoMigrationSpec
