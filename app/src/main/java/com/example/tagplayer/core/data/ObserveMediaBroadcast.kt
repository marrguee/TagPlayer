package com.example.tagplayer.core.data

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.domain.DispatcherList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ObserveMediaBroadcast(
    dispatcherList: DispatcherList,
    private val songsDao: SongsDao,
) : BroadcastReceiver() {
    private val coroutineScope = CoroutineScope(dispatcherList.io())
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        action?.let {
            if (action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
                val downloadId = intent.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID,
                    -1
                )
                handleDownloadComplete(context!!, downloadId)
            }
        }
    }

    private fun handleDownloadComplete(context: Context, downloadId: Long) {
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor = downloadManager.query(query)

        if (cursor.moveToFirst()) {
            val statusIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
            if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(statusIndex)) {
                val uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
                val uri = Uri.parse(uriString)
                if (isMusicFile(context, uri)) {
                    uri?.let {
                        val projection = arrayOf(
                            MediaStore.Audio.Media._ID,
                            MediaStore.Audio.Media.TITLE,
                            MediaStore.Audio.Media.DURATION,
                        )
                        context.contentResolver?.query(
                            uri,
                            projection,
                            null,
                            null,
                            null
                        )?.use { cursor ->
                            if (cursor.moveToFirst()) {
                                val idColumn =
                                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                                val titleColumn =
                                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                                val durationColumn =
                                    cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

                                val id = cursor.getLong(idColumn)
                                val title = cursor.getString(titleColumn)
                                val duration = cursor.getLong(durationColumn)

                                val song = Song(id, title, duration, uri.toString())
                                coroutineScope.launch {
                                    songsDao.addSong(song)
                                }
                            }
                        }
                    }
                }
            }
        }
        cursor.close()
    }

    private fun isMusicFile(context: Context, uri: Uri): Boolean {
        val mimeType = context.contentResolver.getType(uri)
        return mimeType?.startsWith("audio/") == true
    }
}

/*
val uri: Uri? = intent.data
                val projection = arrayOf(
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DURATION,
                )

                uri?.let {
                    context?.contentResolver?.query(
                        uri,
                        projection,
                        null,
                        null,
                        null
                    )?.use { cursor ->
                        if (cursor.moveToFirst()) {
                            val idColumn =
                                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                            val titleColumn =
                                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                            val durationColumn =
                                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

                            val id = cursor.getLong(idColumn)
                            val title = cursor.getString(titleColumn)
                            val duration = cursor.getLong(durationColumn)

                            val song = Song(id, title, duration, uri.toString())
                            coroutineScope.launch {
                                songsDao.addSong(song)
                            }
                        }
                    }
                }
* */