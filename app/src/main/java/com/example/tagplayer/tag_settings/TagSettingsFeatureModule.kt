package com.example.tagplayer.tag_settings

import androidx.lifecycle.ViewModel
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.tag_settings.add_tag.AddTagModule
import com.example.tagplayer.tag_settings.add_tag.presentation.AddTagViewModel
import com.example.tagplayer.tag_settings.presentation.Selected
import com.example.tagplayer.tag_settings.presentation.TagSettingsViewModel

interface TagSettingsFeatureModule : ProvideViewModel {

    class Base(
        private val core: Core,
        private val mutableMap: MutableMap<Class<out ViewModel>, ViewModel>,
        private val clear: () -> Unit
    ) : TagSettingsFeatureModule {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> provide(clazz: Class<out T>): T {
            return if (mutableMap.containsKey(clazz)) mutableMap[clazz] as T
            else {
                val viewModel = when (clazz) {
                    TagSettingsViewModel::class.java ->
                        TagSettingsModule.Base(core, Selected.Tag, clear).create()

                    AddTagViewModel::class.java ->
                        AddTagModule(core, Selected.Tag).create()

                    else -> throw IllegalStateException("Unknown ViewModel class $clazz")
                }
                mutableMap[clazz] = viewModel
                viewModel
            } as T
        }
    }
}