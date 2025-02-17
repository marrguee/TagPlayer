package com.example.tagplayer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.util.Size
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import java.io.IOException

class ContentUriDataFetcher(
    private val context: Context,
    private val uri: Uri,
    private val width: Int,
    private val height: Int
) : DataFetcher<Bitmap> {

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in Bitmap>) {
        try {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver.loadThumbnail(uri, Size(width, height), null)
            } else {
                val legacyUri = uri.toString()
                    .replace("audio/albums", "audio/albumart")
                    .let { Uri.parse(it) }

                context.contentResolver.openInputStream(legacyUri)?.use {
                    BitmapFactory.decodeStream(it)
                }
            }

            bitmap?.let {
                callback.onDataReady(it)
            } ?: callback.onLoadFailed(IOException("Bitmap is null"))

        } catch (e: Exception) {
            callback.onLoadFailed(e)
        }
    }

    override fun cleanup() {}
    override fun cancel() {}
    override fun getDataClass(): Class<Bitmap> = Bitmap::class.java
    override fun getDataSource(): DataSource = DataSource.LOCAL
}
