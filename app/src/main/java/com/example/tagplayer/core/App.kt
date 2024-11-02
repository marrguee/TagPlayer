package com.example.tagplayer.core

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.media3.common.util.UnstableApi
import com.example.tagplayer.core.data.database.dao.LastPlayedDao
import com.example.tagplayer.core.data.database.dao.SongsDao
import com.example.tagplayer.core.domain.ProvideMediaStoreHandler
import com.example.tagplayer.core.domain.ManageResources
import com.example.tagplayer.core.domain.ProvideLastPlayedDao
import com.example.tagplayer.core.domain.ProvideMediaObserver
import com.example.tagplayer.core.domain.ProvideMediaReceiver
import com.example.tagplayer.core.domain.ProvidePlayerService
import com.example.tagplayer.core.domain.ProvideSongsDao
import com.example.tagplayer.core.domain.ProvideViewModel

@UnstableApi
class App : Application(),
    ProvideViewModel,
    ProvideMediaStoreHandler,
    ProvidePlayerService,
    ManageResources.Provide,
    ProvideSongsDao,
    ProvideLastPlayedDao,
    ProvideMediaReceiver,
    ProvideMediaObserver
{
    private lateinit var core: Core
    private lateinit var factory: ProvideViewModel.Factory

    override fun onCreate() {
        super.onCreate()
        core = Core.Base(this, contentResolver)
        val songsFilterPrefs = SharedPrefs.TagFilterSharedPref(
            getSharedPreferences(
                "TagPlayerSharedPreferences",
                MODE_PRIVATE
            )
        )
        factory = ProvideViewModel.Factory(core, songsFilterPrefs)

    }

    override fun start(id: Long) {
        val intent = Intent(this, TagPlayerService::class.java)
        intent.action = TagPlayerService.START_SERVICE
        intent.putExtra(TagPlayerService.ID_KEY, id)
        ContextCompat.startForegroundService(this, intent)
    }

    override fun <T : ViewModel> provide(clazz: Class<out T>) = factory.create(clazz)
    override fun mediaStoreHandler() = core.mediaStoreHandler()
    override fun manageRecourses() = core.manageRecourses()
    override fun songsDao(): SongsDao = core.songsDao()
    override fun lastPlayedDao(): LastPlayedDao = core.lastPlayedDao()
    override fun mediaReceiver(): MediaScannerFinishedReceiver =
        MediaScannerFinishedReceiver(core.foregroundWrapper())
    override fun mediaObserver(): MediaObserver =
        MediaObserver(Handler(Looper.getMainLooper()), core.foregroundWrapper())
}