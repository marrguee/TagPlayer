package com.example.tagplayer.filter_by_tags

import androidx.lifecycle.ViewModel
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.home.presentation.HomeModule
import com.example.tagplayer.home.presentation.HomeViewModel

class HomeAndTagsFilterProvideViewModule(
    private val core: Core,
    private val sharedPrefs: SharedPrefs.Mutable<List<Long>>,
    private val clear: ClearViewModel
) : ProvideViewModel {
    private val selectedTagsObservable: CustomObservable.All<List<Long>> =
        CustomObservable.ManualClear(emptyList())

    override fun <T : ViewModel> provide(clazz: Class<out T>): T {
        return when (clazz) {
            HomeViewModel::class.java -> HomeModule.Base(core, sharedPrefs, selectedTagsObservable)
                .create()

            FilterTagsViewModel::class.java -> TagsFilterModule(
                core,
                sharedPrefs,
                selectedTagsObservable,
                clear
            ).create()

            else -> throw IllegalStateException("Unknown ViewModel")
        } as T
    }
}