package com.example.tagplayer.core.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "last_played")
data class LastPlayed(
    @PrimaryKey
    @ColumnInfo("song_id") val songId: Long,
    @ColumnInfo("date") val date: Date,
)