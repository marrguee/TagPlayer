package com.example.tagplayer.core

import android.app.Application
import android.content.Intent
import android.content.IntentFilter
import androidx.lifecycle.ViewModel
import androidx.work.WorkManager
import com.example.tagplayer.core.data.MediaStoreHandler
import com.example.tagplayer.core.data.ObserveMediaBroadcast
import com.example.tagplayer.core.data.ProvideMediaStoreHandler

class App : Application(), ProvideViewModel, ProvideMediaStoreHandler {
    private lateinit var core: Core
    private lateinit var factory: ProvideViewModel.Factory
    private lateinit var broadcast: ObserveMediaBroadcast
    override fun onCreate() {
        super.onCreate()
        core = Core.Base(this, contentResolver)
        factory = ProvideViewModel.Factory(core)
        broadcast = core.observeMediaBroadcast()
        registerReceiver(
            broadcast,
            IntentFilter(Intent.ACTION_MEDIA_CHECKING)
        )
    }

    override fun <T : ViewModel> provide(clazz: Class<out T>) = factory.create(clazz)
    override fun mediaStoreHandler() = core.mediaStoreHandler()
}