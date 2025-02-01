package com.example.tagplayer.core.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.util.UnstableApi
import com.example.tagplayer.home.presentation.HomeViewModel
import com.example.tagplayer.core.Core
import com.example.tagplayer.edit_song_tags.presentation.EditSongTagsViewModel
import com.example.tagplayer.filter_by_tags.presentation.FilterViewModel
import com.example.tagplayer.filter_by_tags.HomeFilterModule
import com.example.tagplayer.core.SharedPrefs
import com.example.tagplayer.main.presentation.MainViewModel
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.playback_control.presentation.PlaybackControlModule
import com.example.tagplayer.playback_control.presentation.PlaybackControlObservable
import com.example.tagplayer.playback_control.presentation.PlaybackControlViewModel
import com.example.tagplayer.recently.presentation.RecentlyModule
import com.example.tagplayer.recently.presentation.RecentlyViewModel
import com.example.tagplayer.search.presentation.SearchModule
import com.example.tagplayer.search.presentation.SearchViewModel
import com.example.tagplayer.tag_settings.TagSettingsFeatureModule
import com.example.tagplayer.tag_settings.add_tag.AddTagViewModel
import com.example.tagplayer.tag_settings.presentation.TagSettingsViewModel

interface ProvideViewModel {
    fun <T : ViewModel> provide(clazz: Class<out T>): T

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val core: Core,
        songsFilterPrefs: SharedPrefs.Mutable<List<Long>>
    ) : ViewModelProvider.Factory, ClearViewModel {
        private val viewModels: MutableMap<Class<out ViewModel>, ViewModel> = mutableMapOf()

        private var tagSettingsFeatureModule: TagSettingsFeatureModule? = null
        private val homeAndFiltersModule: HomeFilterModule =
            HomeFilterModule(core, songsFilterPrefs, this)

        private val clearTagSettingsModule: () -> Unit = {
            clear(TagSettingsViewModel::class.java)
            clear(AddTagViewModel::class.java)
            tagSettingsFeatureModule = null
        }

        @UnstableApi
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (viewModels.containsKey(modelClass)) {
                viewModels[modelClass]
            } else {
                val viewModel = when (modelClass) {
                    HomeViewModel::class.java,
                    EditSongTagsViewModel::class.java,
                    FilterViewModel::class.java ->
                        homeAndFiltersModule.provide(modelClass)

                    RecentlyViewModel::class.java ->
                        RecentlyModule.Base(core, this).create()

                    SearchViewModel::class.java ->
                        SearchModule.Base(core, this).create()

                    TagSettingsViewModel::class.java -> {
                        if (tagSettingsFeatureModule == null) tagSettingsFeatureModule =
                            TagSettingsFeatureModule.Base(core, viewModels, clearTagSettingsModule)
                        tagSettingsFeatureModule!!.provide(modelClass)
                    }

                    PlaybackControlViewModel::class.java ->
                        PlaybackControlModule.Base().create()

                    MainViewModel::class.java ->
                        MainViewModel(Navigation.Base)

                    AddTagViewModel::class.java -> {
                        if (tagSettingsFeatureModule == null) tagSettingsFeatureModule =
                            TagSettingsFeatureModule.Base(core, viewModels, clearTagSettingsModule)
                        tagSettingsFeatureModule!!.provide(modelClass)
                    }

                    else -> throw IllegalStateException("ViewModel class $modelClass have not been founded")
                }
                viewModels[modelClass] = viewModel
                viewModel
            } as T
        }

        override fun clear(clazz: Class<out ViewModel>) {
            viewModels.remove(clazz)
        }
    }
}