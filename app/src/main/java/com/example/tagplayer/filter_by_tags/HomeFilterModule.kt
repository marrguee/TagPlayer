package com.example.tagplayer.filter_by_tags

import androidx.lifecycle.ViewModel
import com.example.tagplayer.core.Core
import com.example.tagplayer.core.CustomObservable
import com.example.tagplayer.core.SharedPrefs
import com.example.tagplayer.core.domain.ClearViewModel
import com.example.tagplayer.core.domain.ProvideViewModel
import com.example.tagplayer.edit_song_tags.presentation.EditSongTagModule
import com.example.tagplayer.edit_song_tags.presentation.EditSongTagsViewModel
import com.example.tagplayer.filter_by_tags.presentation.FilterViewModel
import com.example.tagplayer.filter_by_tags.presentation.FilterModule
import com.example.tagplayer.home.presentation.HomeModule
import com.example.tagplayer.home.presentation.HomeViewModel
import com.example.tagplayer.home.presentation.TagFilterObservable
import com.example.tagplayer.home.presentation.TagFiltersState
import java.util.concurrent.atomic.AtomicLong

class HomeFilterModule(
    private val core: Core,
    private val songsFilterPrefs: SharedPrefs.Mutable<List<Long>>,
    private val clear: ClearViewModel
) : ProvideViewModel {
    private val tagFiltersObservable: CustomObservable.AllHandleState<TagFiltersState> =
        TagFilterObservable()
    private val selectedSongId: AtomicLong = AtomicLong(Long.MIN_VALUE)

    override fun <T : ViewModel> provide(clazz: Class<out T>): T {
        return when (clazz) {
            HomeViewModel::class.java -> HomeModule.Base(
                core,
                songsFilterPrefs,
                tagFiltersObservable,
                selectedSongId
            ).create()

            FilterViewModel::class.java -> FilterModule(
                core,
                songsFilterPrefs,
                tagFiltersObservable,
                clear
            ).create()

            EditSongTagsViewModel::class.java -> EditSongTagModule(
                core,
                clear,
                selectedSongId
            ).create()

            else -> throw IllegalStateException("Unknown ViewModel")
        } as T
    }
}