package com.example.tagplayer

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey

class ContentUriModelLoader(val context: Context) : ModelLoader<Uri, Bitmap> {
    override fun buildLoadData(
        uri: Uri,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<Bitmap> {
        return ModelLoader.LoadData(
            ObjectKey(uri),
            ContentUriDataFetcher(context, uri, width, height)
        )
    }

    override fun handles(uri: Uri): Boolean {
        return uri.toString().contains("audio/albums")
    }

    class Factory(val context: Context) :
        ModelLoaderFactory<Uri, Bitmap> {

        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<Uri, Bitmap> {
            return ContentUriModelLoader(context)
        }

        override fun teardown() {}
    }
}
