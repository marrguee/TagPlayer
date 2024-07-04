package com.example.tagplayer.core.data.database

import androidx.room.TypeConverter
import java.util.Date

class TimestampConverter {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }
}