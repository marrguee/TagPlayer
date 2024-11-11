package com.example.tagplayer.filter_by_tags

import androidx.lifecycle.ViewModel
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.SharedPrefs
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.filter_by_tags.presentation.FilterTagsViewModel
import com.example.tagplayer.filter_by_tags.presentation.TagsFilterModule
import com.example.tagplayer.home.presentation.HomeModule
import com.example.tagplayer.home.presentation.HomeViewModel
import com.example.tagplayer.home.presentation.TagFilterObservable
import com.example.tagplayer.home.presentation.TagFiltersResponse

class HomeAndTagsFilterProvideViewModule(
    private val core: Core,
    private val songsFilterPrefs: SharedPrefs.Mutable<List<Long>>,
    private val clear: ClearViewModel
) : ProvideViewModel {
    private val tagFiltersObservable: CustomObservable.Mutable<TagFiltersResponse> = TagFilterObservable()

    override fun <T : ViewModel> provide(clazz: Class<out T>): T {
        return when (clazz) {
            HomeViewModel::class.java -> HomeModule.Base(
                core,
                songsFilterPrefs,
                tagFiltersObservable
            ).create()

            FilterTagsViewModel::class.java -> TagsFilterModule(
                core,
                songsFilterPrefs,
                tagFiltersObservable,
                clear
            ).create()

            else -> throw IllegalStateException("Unknown ViewModel")
        } as T
    }
}