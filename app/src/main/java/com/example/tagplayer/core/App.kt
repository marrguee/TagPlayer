package com.example.tagplayer.core

import android.app.Application
import androidx.lifecycle.ViewModel

class App : Application(), ProvideViewModel {
    private lateinit var core: Core
    private lateinit var factory: ProvideViewModel.Factory
    override fun onCreate() {
        super.onCreate()
        core = Core.Base(contentResolver)
        factory = ProvideViewModel.Factory(core)
    }

    override fun <T : ViewModel> provide(clazz: Class<out T>) = factory.create(clazz)
}