package com.example.tagplayer.tagsettings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.tagsettings.add_tag.AddTagModule
import com.example.tagplayer.tagsettings.add_tag.AddTagViewModel
import com.example.tagplayer.tagsettings.presentation.TagSettingsModule
import com.example.tagplayer.tagsettings.presentation.TagSettingsUi
import com.example.tagplayer.tagsettings.presentation.TagSettingsViewModel

interface TagSettingsFeatureModule : ProvideViewModel {

    class Base(
        private val core: Core,
        private val mutableMap: MutableMap<Class<out ViewModel>, ViewModel>,
        private val clear: () -> Unit
    ) : TagSettingsFeatureModule {

        private val selectedTagLiveData = MutableLiveData<TagSettingsUi?>()

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> provide(clazz: Class<out T>): T {
            return if (mutableMap.containsKey(clazz)) mutableMap[clazz] as T
            else {
                val viewModel = when (clazz) {
                    TagSettingsViewModel::class.java ->
                        TagSettingsModule.Base(core, selectedTagLiveData, clear).create()

                    AddTagViewModel::class.java ->
                        AddTagModule(core, selectedTagLiveData).create()

                    else -> throw IllegalStateException("Unknown ViewModel class $clazz")
                }
                mutableMap[clazz] = viewModel
                viewModel
            } as T
        }
    }
}