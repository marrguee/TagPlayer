package com.example.tagplayer.core.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tags",
    indices = [
        Index(value = ["title"], unique = true)
    ]
)
data class SongTag(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Long,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("color") val color: String
)