package com.example.tagplayer.core.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tagplayer.all.presentation.AllFeatureViewModel
import com.example.tagplayer.all.presentation.AllModule
import com.example.tagplayer.core.Core
import com.example.tagplayer.history.presentation.HistoryModule
import com.example.tagplayer.search.presentation.SearchModule
import com.example.tagplayer.tagsettings.presentation.TagSettingsModule
import com.example.tagplayer.history.presentation.HistoryViewModel
import com.example.tagplayer.search.presentation.SearchViewModel
import com.example.tagplayer.tagsettings.presentation.TagSettingsViewModel

interface ProvideViewModel {
    fun <T : ViewModel> provide(clazz: Class<out T>) : T

    @Suppress("UNCHECKED_CAST")
    class Factory(
        private val core: Core,
    ) : ViewModelProvider.Factory, ClearViewModel {
        private val viewModels: MutableMap<Class<out ViewModel>, ViewModel> = mutableMapOf()
        private val clearSearchModule: () -> Unit = {
            clear(SearchViewModel::class.java)
        }
        private val clearTagSettingsModule: () -> Unit = {
            clear(TagSettingsViewModel::class.java)
        }
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (viewModels.containsKey(modelClass)) {
                viewModels[modelClass]
            }
            else {
                val viewModel = when (modelClass) {
                    AllFeatureViewModel::class.java ->
                        AllModule.Base(core).create()
                    HistoryViewModel::class.java ->
                        HistoryModule.Base(core).create()
                    SearchViewModel::class.java ->
                        SearchModule.Base(core, clearSearchModule).create()
                    TagSettingsViewModel::class.java ->
                        TagSettingsModule.Base(core, clearTagSettingsModule).create()
                    else -> throw IllegalStateException("ViewModel class have not been founded")
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