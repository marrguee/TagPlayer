package com.example.tagplayer.core

import android.content.ContentResolver
import android.content.Context
import androidx.core.content.ContextCompat
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.WorkManager
import com.example.tagplayer.R
import com.example.tagplayer.home.data.ExtractMedia
import com.example.tagplayer.core.data.database.MediaDatabase
import com.example.tagplayer.home.data.HandleMediaStore
import com.example.tagplayer.core.domain.ProvideMediaStoreHandler
import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.data.ForegroundWrapper
import com.example.tagplayer.core.domain.DomainError
import com.example.tagplayer.core.domain.HandleError
import com.example.tagplayer.core.domain.ManageResources
import com.example.tagplayer.core.domain.ProvideLastPlayedDao
import com.example.tagplayer.core.domain.ProvideTagDao
import com.example.tagplayer.core.presentation.ShareRequest
import com.example.tagplayer.home.data.ExtractMediaResult
import com.example.tagplayer.playback.presentation.HandleConnection


interface Core :
    ProvideMediaStoreHandler,
    ManageResources.Provide,
    ProvideLastPlayedDao,
    ProvideTagDao
{
    fun songsDao() : SongsDao
    fun mediaDatabase() : MediaDatabase
    fun foregroundWrapper() : ForegroundWrapper
    fun shareRequest() : ShareRequest
    fun handleConnection() : HandleConnection
    fun handlePresentationError() : HandleError<DomainError, String>

    class Base(
        context: Context,
        contentResolver: ContentResolver
    ) : Core {
        private val mediaDatabase = Room.databaseBuilder(
            context,
            MediaDatabase::class.java,
            ContextCompat.getString(context, R.string.database_name)
        ).build()
        private val foregroundWrapper = ForegroundWrapper.Base(WorkManager.getInstance(context))
        private val shareRequest = ShareRequest.Base(context)
        private val manageResources = ManageResources.Base(context)
        private val handleConnection = HandleConnection.Base(context)
        private val handlePresentationError = HandleError.Presentation(context)
        private val handleMediaStore = HandleMediaStore.Base(
            ExtractMedia.Base(contentResolver),
            ExtractMediaResult.Mapper.Base(mediaDatabase.songsDao),
            mediaDatabase.songsDao
        )

        override fun songsDao() = mediaDatabase.songsDao
        override fun lastPlayedDao() = mediaDatabase.lastPlayed
        override fun tagDao() = mediaDatabase.tagsDao
        override fun manageRecourses() = manageResources
        override fun mediaDatabase() = mediaDatabase
        override fun foregroundWrapper() = foregroundWrapper
        override fun shareRequest() = shareRequest
        override fun handleConnection() = handleConnection
        override fun handlePresentationError() = handlePresentationError
        override fun mediaStoreHandler() = handleMediaStore
    }

}
