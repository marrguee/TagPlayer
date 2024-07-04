package com.example.tagplayer.core.domain

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.tagplayer.all.presentation.AllState
import com.example.tagplayer.history.presentation.HistoryState
import com.example.tagplayer.search.domain.SearchState
import com.example.tagplayer.tagsettings.presentation.TagSettingsState

interface Communication<T> {
    fun update(data: T)
    fun observe(owner: LifecycleOwner, observer: Observer<in T>)

    class AllCommunication : Communication<AllState> {
        private val liveData = MutableLiveData<AllState>()
        override fun update(data: AllState) {
            liveData.value = data
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in AllState>) {
            liveData.observe(owner, observer)
        }
    }

    class HistoryCommunication : Communication<HistoryState> {
        private val liveData = MutableLiveData<HistoryState>()
        override fun update(data: HistoryState) {
            liveData.value = data
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in HistoryState>) {
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
}