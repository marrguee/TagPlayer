package com.example.tagplayer.core.domain

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.tagplayer.all.presentation.HomeState
import com.example.tagplayer.recently.presentation.RecentlyState
import com.example.tagplayer.playback_control.presentation.PlaybackControlState
import com.example.tagplayer.search.domain.SearchState
import com.example.tagplayer.tagsettings.add_tag.TagDialogState
import com.example.tagplayer.tagsettings.presentation.TagSettingsState

interface Communication<T> {
    fun update(data: T)
    fun observe(owner: LifecycleOwner, observer: Observer<in T>)

    class AllCommunication : Communication<HomeState> {
        private val liveData = MutableLiveData<HomeState>()
        override fun update(data: HomeState) {
            liveData.value = data
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in HomeState>) {
            liveData.observe(owner, observer)
        }
    }

    class HistoryCommunication : Communication<RecentlyState> {
        private val liveData = MutableLiveData<RecentlyState>()
        override fun update(data: RecentlyState) {
            liveData.value = data
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in RecentlyState>) {
            liveData.observe(owner, observer)
        }
    }

    class SearchCommunication : Communication<SearchState> {
        private val liveData = MutableLiveData<SearchState>()
        override fun update(data: SearchState) {
            liveData.value = data
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in SearchState>) {
            liveData.observe(owner, observer)
        }
    }

    class TagSettingsCommunication : Communication<TagSettingsState> {
        private val liveData = MutableLiveData<TagSettingsState>()
        override fun update(data: TagSettingsState) {
            liveData.value = data
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in TagSettingsState>) {
            liveData.observe(owner, observer)
        }
    }

    class PlaybackControlCommunication : Communication<PlaybackControlState> {
        private val liveData = MutableLiveData<PlaybackControlState>()
        override fun update(data: PlaybackControlState) {
            liveData.value = data
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in PlaybackControlState>) {
            liveData.observe(owner, observer)
        }
    }

    class TagDialogCommunication : Communication<TagDialogState> {
        private val liveData = MutableLiveData<TagDialogState>()
        override fun update(data: TagDialogState) {
            liveData.value = data
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in TagDialogState>) {
            liveData.observe(owner, observer)
        }
    }
}