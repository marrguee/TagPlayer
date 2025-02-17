package com.example.tagplayer.home.data

import android.app.RecoverableSecurityException
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.VOLUME_EXTERNAL_PRIMARY
import android.util.Log
import android.util.Size
import androidx.media3.common.util.UnstableApi
import com.bumptech.glide.Glide
import com.example.tagplayer.core.data.database.models.Song
import java.io.IOException


interface ExtractMedia {
    suspend fun media(): List<Song>
    suspend fun scanNewFile(uri: Uri): Song?
    suspend fun deleteSong(songId: Long): ExtractMediaResult
    suspend fun mediaStoreChanged(context: Context): Boolean

    class Base(private val contentResolver: ContentResolver) : ExtractMedia {
        private var mediaStoreVersion: String = String()
        private var generationVersion: Long = 0
        private val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.DATE_MODIFIED
        )


        override suspend fun media(): List<Song> {
            val result = mutableListOf<Song>()
            val selection =
                "${MediaStore.Audio.Media.IS_MUSIC} = ? AND ${MediaStore.Audio.Media.DURATION} > ?"
            val selectionArgs = arrayOf("1", "1000")

            contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
            )?.use { c ->
                val idColumn = c.getColumnIndex(MediaStore.Audio.Media._ID)
                val titleColumn = c.getColumnIndex(MediaStore.Audio.Media.TITLE)
                val authorColumn = c.getColumnIndex(MediaStore.Audio.Media.ARTIST)
                val durationColumn = c.getColumnIndex(MediaStore.Audio.Media.DURATION)
                val albumColumn = c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                val dateModifiedColumn = c.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED)
                while (c.moveToNext()) {
                    val id = c.getLong(idColumn)
                    val album = c.getLong(albumColumn)
                    val title = c.getString(titleColumn)
                    val duration = c.getLong(durationColumn)
                    val author = c.getString(authorColumn)
                    val uri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        id
                    )
                    val dateModified = c.getLong(dateModifiedColumn)
                    val image = ContentUris.withAppendedId(
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI
                        else
                            Uri.parse("content://media/external/audio/albumart"),
                        album
                    )

                    result.add(
                        Song(
                            id,
                            image.toString(),
                            title,
                            author,
                            duration,
                            uri.toString(),
                            dateModified
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
                    val albumColumn = c.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
                    val titleColumn = c.getColumnIndex(MediaStore.Audio.Media.TITLE)
                    val durationColumn = c.getColumnIndex(MediaStore.Audio.Media.DURATION)
                    val authorColumn = c.getColumnIndex(MediaStore.Audio.Media.AUTHOR)
                    val dateModifiedColumn = c.getColumnIndex(MediaStore.Audio.Media.DATE_MODIFIED)

                    val id = c.getLong(idColumn)
                    val album = c.getString(albumColumn)
                    val title = c.getString(titleColumn)
                    val author = c.getString(authorColumn)
                    val duration = c.getLong(durationColumn)
                    val dateModified = c.getLong(dateModifiedColumn)

                    Song(id, album, title, author, duration, uri.toString(), dateModified)
                } else {
                    null
                }
            }

        }

        override suspend fun deleteSong(songId: Long): ExtractMediaResult {
            val uri = ContentUris
                .withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId)
            val cursor = contentResolver.query(
                uri,
                arrayOf(MediaStore.Audio.Media._ID),
                null,
                null,
                null
            )
            val fileExists = cursor?.use { it.moveToFirst() } ?: false

            if (!fileExists) return ExtractMediaResult.AlreadyDeleted

            return try {
                val deletedRows = contentResolver.delete(uri, null, null)
                if (deletedRows > 0) ExtractMediaResult.DeletingWithoutPermission(songId)
                else ExtractMediaResult.Error("Deleted rows less than 0")
            } catch (e: SecurityException) {
                val intentSender = when {
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                        MediaStore.createDeleteRequest(contentResolver, listOf(uri)).intentSender
                    }
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                        val recoverableSecurityException = e as? RecoverableSecurityException
                        recoverableSecurityException?.userAction?.actionIntent?.intentSender
                    }
                    else -> null
                }
                if (intentSender == null) ExtractMediaResult.Error("Cannot get intentSender")
                else ExtractMediaResult.DeletingWithPermission(intentSender)
            }
        }

        override suspend fun mediaStoreChanged(context: Context): Boolean {
            val version =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    MediaStore.getVersion(context, VOLUME_EXTERNAL_PRIMARY)
                else
                    MediaStore.getVersion(context)

            if (mediaStoreVersion == version) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val newGeneration = MediaStore.getGeneration(context, VOLUME_EXTERNAL_PRIMARY)
                    if (generationVersion == newGeneration) return false
                    generationVersion = newGeneration
                    return true
                }
                return false
            } else {
                mediaStoreVersion = version
                return true
            }
        }
    }
}