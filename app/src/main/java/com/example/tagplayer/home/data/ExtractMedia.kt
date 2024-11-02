package com.example.tagplayer.home.data

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.ContentObserver
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import com.example.tagplayer.core.data.database.models.Song

interface ExtractMedia {
    suspend fun media(): List<Song>
    suspend fun scanNewFile(uri: Uri): Song?
    suspend fun mediaStoreChanged(): Boolean

    class Base(
        private val contentResolver: ContentResolver,
        private val context: Context
    ) : ExtractMedia {
        private var lastVersion = String()
        private val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DURATION,
        )

        override suspend fun media(): List<Song> {
            val result = mutableListOf<Song>()
            val selection =
                "${MediaStore.Audio.Media.IS_MUSIC} = ? AND ${MediaStore.Audio.Media.DURATION} > ?"
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
                        Song(
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

        override suspend fun scanNewFile(uri: Uri): Song? {
            val cursor = contentResolver.query(
                uri,
                projection,
                null,
                null,
                null
            )

            return cursor?.use { c ->
                if (c.moveToFirst()) {
                    val idColumn = c.getColumnIndex(MediaStore.Audio.Media._ID)
                    val nameColumn = c.getColumnIndex(MediaStore.Audio.Media.TITLE)
                    val durationColumn = c.getColumnIndex(MediaStore.Audio.Media.DURATION)

                    val id = c.getLong(idColumn)
                    val name = c.getString(nameColumn)
                    val duration = c.getLong(durationColumn)

                    Song(id, name, duration, uri.toString())
                } else {
                    null
                }
            }

        }

        override suspend fun mediaStoreChanged() = MediaStore.getVersion(context).let {
            if (it == lastVersion) false
            else {
                lastVersion = it
                true
            }
        }
    }
}