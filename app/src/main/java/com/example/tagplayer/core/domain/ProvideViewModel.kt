package com.example.tagplayer.core.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tagplayer.core.Core
import com.example.tagplayer.tags_attach.AttachTagsModule
import com.example.tagplayer.tags_attach.presentation.AttachTagsViewModel
import com.example.tagplayer.filter.FilterModule
import com.example.tagplayer.filter.presentation.FilterViewModel
import com.example.tagplayer.home.HomeModule
import com.example.tagplayer.home.presentation.HomeViewModel
import com.example.tagplayer.main.presentation.MainViewModel
import com.example.tagplayer.main.presentation.navigation.Navigation
import com.example.tagplayer.playback.PlaybackModule
import com.example.tagplayer.playback.presentation.PlaybackViewModel
import com.example.tagplayer.recently.RecentlyModule
import com.example.tagplayer.recently.presentation.RecentlyViewModel
import com.example.tagplayer.search.SearchModule
import com.example.tagplayer.search.presentation.SearchViewModel
import com.example.tagplayer.tag_settings.TagSettingsModule
import com.example.tagplayer.tag_details.TagDetailsModule
import com.example.tagplayer.tag_details.presentation.AddTagViewModel
import com.example.tagplayer.tag_details.presentation.EditTagViewModel
import com.example.tagplayer.tag_details.presentation.radio_grid.RadioGridModule
import com.example.tagplayer.tag_details.presentation.radio_grid.RadioGridViewModel
import com.example.tagplayer.tag_settings.presentation.TagSettingsViewModel

interface ProvideViewModel {
    fun <T : ViewModel> provide(clazz: Class<out T>): T

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val core: Core
    ) : ViewModelProvider.Factory, ClearViewModel {
        private val viewModels: MutableMap<Class<out ViewModel>, ViewModel> = mutableMapOf()

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (viewModels.containsKey(modelClass)) {
                viewModels[modelClass]
            } else {
                val viewModel = when (modelClass) {
                    MainViewModel::class.java ->
                        MainViewModel(Navigation.Base)

                    HomeViewModel::class.java ->
                        HomeModule(core).create()

                    FilterViewModel::class.java ->
                        FilterModule(core, this).create()

                    AttachTagsViewModel::class.java ->
                        AttachTagsModule(core, this).create()

                    SearchViewModel::class.java ->
                        SearchModule(core, this).create()

                    RecentlyViewModel::class.java ->
                        RecentlyModule.Base(core, this).create()

                    PlaybackViewModel::class.java ->
                        PlaybackModule.Base(core).create()

                    TagSettingsViewModel::class.java ->
                        TagSettingsModule.Base(core, this).create()

                    AddTagViewModel::class.java,
                    EditTagViewModel::class.java ->
                        TagDetailsModule(core, this, modelClass).create()

                    RadioGridViewModel::class.java ->
                        RadioGridModule(this).create()

                    else -> throw IllegalStateException(
                        "ViewModel class $modelClass have not been founded"
                    )
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