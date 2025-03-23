package com.example.tagplayer.tags_attach.presentation

import android.content.Context
import android.widget.Toast
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.core.presentation.custom_views.interfaces.HideAndShow

interface AttachTagsState {
    fun dispatch(
        context: Context,
        allAdapter: AttachTagsAdapter,
        ownedAdapter: AttachTagsAdapter,
        allPlaceholder: HideAndShow,
        ownPlaceholder: HideAndShow
    )
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = viewModel.clear()

    data class DragAndDrop(
        private val allTags: List<TagUi>,
        private val ownedTags: List<TagUi>,
    ) : AttachTagsState {
        override fun dispatch(
            context: Context,
            allAdapter: AttachTagsAdapter,
            ownedAdapter: AttachTagsAdapter,
            allPlaceholder: HideAndShow,
            ownPlaceholder: HideAndShow
        ) {
            allAdapter.submitList(allTags) {
                allPlaceholder.run { if (allTags.isEmpty()) show() else hide() }
            }

            ownedAdapter.submitList(ownedTags) {
                ownPlaceholder.run { if (ownedTags.isEmpty()) show() else hide() }
            }
        }

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit
    }

    data class Error(private val error: String) : AttachTagsState {
        override fun dispatch(
            context: Context,
            allAdapter: AttachTagsAdapter,
            ownedAdapter: AttachTagsAdapter,
            allPlaceholder: HideAndShow,
            ownPlaceholder: HideAndShow
        ) = Toast.makeText(context, error, Toast.LENGTH_LONG).show()
    }

    object Empty : AttachTagsState {
        override fun dispatch(
            context: Context,
            allAdapter: AttachTagsAdapter,
            ownedAdapter: AttachTagsAdapter,
            allPlaceholder: HideAndShow,
            ownPlaceholder: HideAndShow
        ) = Unit
    }
}