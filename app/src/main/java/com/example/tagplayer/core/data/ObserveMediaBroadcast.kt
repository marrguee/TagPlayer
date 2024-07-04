package com.example.tagplayer.core.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import com.example.tagplayer.core.data.database.models.Song
import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.domain.DispatcherList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ObserveMediaBroadcast(
    dispatcherList: DispatcherList,
    private val songsDao: SongsDao,
) : BroadcastReceiver() {
    private val coroutineScope = CoroutineScope(dispatcherList.io())
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        action?.let {
            if (action == Intent.ACTION_MEDIA_SCANNER_FINISHED) {
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

            }
        }
    }
}