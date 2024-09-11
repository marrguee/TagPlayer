package com.example.tagplayer.core.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tagplayer.home.presentation.HomeModule
import com.example.tagplayer.home.presentation.HomeViewModel
import com.example.tagplayer.core.Core
import com.example.tagplayer.edit_song_tag.EditSongTagModule
import com.example.tagplayer.edit_song_tag.EditSongTagsViewModel
import com.example.tagplayer.filter_by_tags.FilterTagsViewModel
import com.example.tagplayer.filter_by_tags.HomeAndTagsFilterProvideViewModule
import com.example.tagplayer.filter_by_tags.SharedPrefs
import com.example.tagplayer.main.presentation.MainViewModel
import com.example.tagplayer.main.presentation.Navigation
import com.example.tagplayer.playback_control.presentation.PlaybackControlViewModel
import com.example.tagplayer.recently.presentation.RecentlyModule
import com.example.tagplayer.recently.presentation.RecentlyViewModel
import com.example.tagplayer.search.presentation.SearchModule
import com.example.tagplayer.search.presentation.SearchViewModel
import com.example.tagplayer.tagsettings.TagSettingsFeatureModule
import com.example.tagplayer.tagsettings.add_tag.AddTagViewModel
import com.example.tagplayer.tagsettings.presentation.TagSettingsViewModel

interface ProvideViewModel {
    fun <T : ViewModel> provide(clazz: Class<out T>): T

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val core: Core,
        private val sharedPrefs: SharedPrefs.Mutable<List<Long>>,
    ) : ViewModelProvider.Factory, ClearViewModel {
        private val viewModels: MutableMap<Class<out ViewModel>, ViewModel> = mutableMapOf()
        //todo memory leak. View models does not cleared
        private val clearSearchModule: () -> Unit = {
            clear(SearchViewModel::class.java)
        }
        private var tagSettingsFeatureModule: TagSettingsFeatureModule? = null
        private val homeAndFiltersModule: HomeAndTagsFilterProvideViewModule =
            HomeAndTagsFilterProvideViewModule(core, sharedPrefs)

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
                    HomeViewModel::class.java ->
                        homeAndFiltersModule.provide(modelClass)

                    RecentlyViewModel::class.java ->
                        RecentlyModule.Base(core).create()

                    SearchViewModel::class.java ->
                        SearchModule.Base(core, clearSearchModule).create()

                    TagSettingsViewModel::class.java -> {
                        if (tagSettingsFeatureModule == null) tagSettingsFeatureModule =
                            TagSettingsFeatureModule.Base(core, viewModels, clearTagSettingsModule)
                        tagSettingsFeatureModule!!.provide(modelClass)
                    }

                    PlaybackControlViewModel::class.java ->
                        PlaybackControlViewModel(Communication.PlaybackControlCommunication())

                    MainViewModel::class.java ->
                        MainViewModel(Navigation.Base)

                    AddTagViewModel::class.java -> {
                        if (tagSettingsFeatureModule == null) tagSettingsFeatureModule =
                            TagSettingsFeatureModule.Base(core, viewModels, clearTagSettingsModule)
                        tagSettingsFeatureModule!!.provide(modelClass)
                    }

                    EditSongTagsViewModel::class.java -> EditSongTagModule(core).create()

                    FilterTagsViewModel::class.java -> homeAndFiltersModule.provide(modelClass)

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