package com.example.tagplayer.filter_by_tags

import androidx.lifecycle.ViewModel
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.SharedPrefs
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.filter_by_tags.presentation.FilterViewModel
import com.example.tagplayer.filter_by_tags.presentation.FilterModule
import com.example.tagplayer.home.HomeModule
import com.example.tagplayer.home.presentation.HomeViewModel
import com.example.tagplayer.home.presentation.TagFiltersState
import kotlinx.coroutines.flow.MutableStateFlow

@Suppress("UNCHECKED_CAST")
class HomeFilterModule(
    private val core: Core,
    private val songsFilterPrefs: SharedPrefs.Mutable<List<Long>>,
    private val clear: ClearViewModel
) : ProvideViewModel {
    private val tagFiltersObservable: MutableStateFlow<TagFiltersState> =
        MutableStateFlow(TagFiltersState.Empty)

    override fun <T : ViewModel> provide(clazz: Class<out T>): T {
        return when (clazz) {
            HomeViewModel::class.java -> HomeModule(
                core,
                songsFilterPrefs,
                tagFiltersObservable
            ).create()

            FilterViewModel::class.java -> FilterModule(
                core,
                songsFilterPrefs,
                tagFiltersObservable,
                clear
            ).create()

            else -> throw IllegalStateException("Unknown ViewModel")
        } as T
    }
}