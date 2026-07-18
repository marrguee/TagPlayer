package com.example.tagplayer.core

import android.app.Application
import com.bumptech.glide.Glide
import com.example.tagplayer.core.di.coreModule
import com.example.tagplayer.filter.filterModule
import com.example.tagplayer.home.homeModule
import com.example.tagplayer.playback.playbackModule
import com.example.tagplayer.recently.recentlyModule
import com.example.tagplayer.search.searchModule
import com.example.tagplayer.tag_details.presentation.radio_grid.radioGridModule
import com.example.tagplayer.tag_details.tagDetailsModule
import com.example.tagplayer.tag_settings.tagSettingsModule
import com.example.tagplayer.tags_attach.attachTagsModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.annotation.KoinViewModelScopeApi
import org.koin.core.context.startKoin
import org.koin.core.option.viewModelScopeFactory

@OptIn(KoinViewModelScopeApi::class)
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            options(viewModelScopeFactory())
            androidContext(this@App)
            workManagerFactory()
            modules(
                coreModule,
                homeModule,
                filterModule,
                attachTagsModule,
                searchModule,
                recentlyModule,
                playbackModule,
                tagSettingsModule,
                tagDetailsModule,
                radioGridModule
            )
        }
        Glide.get(this)
    }
}