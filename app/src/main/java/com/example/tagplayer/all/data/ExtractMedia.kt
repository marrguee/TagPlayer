package com.example.tagplayer.all.data

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import com.example.tagplayer.core.data.SongData

interface ExtractMedia {
    suspend fun media() : List<SongData>
    class Base(
        private val contentResolver: ContentResolver
    ) : ExtractMedia {

        override suspend fun media(): List<SongData> {
            val result = mutableListOf<SongData>()
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
            )
            val selection = "${MediaStore.Audio.Media.IS_MUSIC} = ? AND ${MediaStore.Audio.Media.DURATION} > ?"
            val selectionArgs = arrayOf("1", "1000")
            val sortOrder = "${MediaStore.Audio.Media.TITLE} DESC"
            contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )?.use { cursor ->
                val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
                val nameColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val durationColumn = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)
                    val duration = cursor.getLong(durationColumn)
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    result.add(
                        SongData(
                            id,
                            name,
                            duration,
                            uri.toString()
                        )
                    )
                }
            }
            return result
        }
    }
}