package com.example.tagplayer.core.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tagplayer.home.presentation.HomeViewModel
import com.example.tagplayer.core.Core
import com.example.tagplayer.edit_song_tags.presentation.EditSongTagsViewModel
import com.example.tagplayer.filter_by_tags.presentation.FilterViewModel
import com.example.tagplayer.filter_by_tags.HomeFilterModule
import com.example.tagplayer.core.SharedPrefs
import com.example.tagplayer.edit_song_tags.EditSongTagModule
import com.example.tagplayer.main.presentation.MainViewModel
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.playback.PlaybackControlModule
import com.example.tagplayer.playback.presentation.PlaybackViewModel
import com.example.tagplayer.recently.RecentlyModule
import com.example.tagplayer.recently.presentation.RecentlyViewModel
import com.example.tagplayer.search.SearchModule
import com.example.tagplayer.search.presentation.SearchViewModel
import com.example.tagplayer.tag_settings.TagSettingsFeatureModule
import com.example.tagplayer.tag_settings.add_tag.presentation.AddTagViewModel
import com.example.tagplayer.tag_settings.add_tag.presentation.radio_grid.RadioGridModule
import com.example.tagplayer.tag_settings.add_tag.presentation.radio_grid.RadioGridViewModel
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

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (viewModels.containsKey(modelClass)) {
                viewModels[modelClass]
            } else {
                val viewModel = when (modelClass) {
                    HomeViewModel::class.java,
                    FilterViewModel::class.java ->
                        homeAndFiltersModule.provide(modelClass)

                    EditSongTagsViewModel::class.java ->
                        EditSongTagModule(core, this).create()

                    SearchViewModel::class.java ->
                        SearchModule(core, this).create()

                    RecentlyViewModel::class.java ->
                        RecentlyModule.Base(core, this).create()

                    TagSettingsViewModel::class.java -> {
                        if (tagSettingsFeatureModule == null) tagSettingsFeatureModule =
                            TagSettingsFeatureModule.Base(core, viewModels, clearTagSettingsModule)
                        tagSettingsFeatureModule!!.provide(modelClass)
                    }

                    PlaybackViewModel::class.java ->
                        PlaybackControlModule.Base(core).create()

                    MainViewModel::class.java ->
                        MainViewModel(Navigation.Base)

                    AddTagViewModel::class.java -> {
                        if (tagSettingsFeatureModule == null) tagSettingsFeatureModule =
                            TagSettingsFeatureModule.Base(core, viewModels, clearTagSettingsModule)
                        tagSettingsFeatureModule!!.provide(modelClass)
                    }

                    RadioGridViewModel::class.java -> RadioGridModule().create()

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