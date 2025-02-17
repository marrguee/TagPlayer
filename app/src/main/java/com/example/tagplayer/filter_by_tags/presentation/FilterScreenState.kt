package com.example.tagplayer.filter_by_tags.presentation

import android.os.Parcelable
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import kotlinx.parcelize.Parcelize

interface FilterScreenState : Parcelable {
    fun dispatch(adapter: FilterAdapter)
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit

    @Parcelize
    class SelectedChangedScreen(
        private val list: List<FilterUi>
    ) : FilterScreenState {
        override fun dispatch(adapter: FilterAdapter) {
            adapter.submitList(list)
        }
    }

    @Parcelize
    object Empty : FilterScreenState {
        override fun dispatch(adapter: FilterAdapter) = Unit
    }
}