package com.example.tagplayer.core.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "songs_and_tags",
    indices = [
        Index(value = ["track_id"]),
        Index(value = ["tag_id"]),
    ],
    primaryKeys = [
        "track_id",
        "tag_id",
    ],
    foreignKeys = [
        ForeignKey(
            entity = Song::class,
            parentColumns = ["id"],
            childColumns = ["track_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = SongTag::class,
            parentColumns = ["id"],
            childColumns = ["tag_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
    ]
)
class SongTagCrossRef(
    @ColumnInfo("track_id") val trackId: Long,
    @ColumnInfo("tag_id") val tagId: Long
)