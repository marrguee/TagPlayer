package com.example.tagplayer.edit_song_tags.presentation

import android.os.Parcelable
import com.example.tagplayer.R
import com.example.tagplayer.core.CustomTextView
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import kotlinx.parcelize.Parcelize

interface EditSongTagState : Parcelable {
    fun dispatch(
        allAdapter: EditSongTagListenerAdapter,
        ownedAdapter: EditSongTagListenerAdapter,
        noAllTagsTextView: CustomTextView,
        noOwnedTagsTextView: CustomTextView
    )
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = viewModel.clear()

    @Parcelize
    data class DragAndDrop(
        private val allTags: List<TagUi>,
        private val ownedTags: List<TagUi>,
    ) : EditSongTagState {
        override fun dispatch(
            allAdapter: EditSongTagListenerAdapter,
            ownedAdapter: EditSongTagListenerAdapter,
            noAllTagsTextView: CustomTextView,
            noOwnedTagsTextView: CustomTextView
        ) {
            allAdapter.submitList(allTags)
            ownedAdapter.submitList(ownedTags)
        }

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit
    }

    @Parcelize
    data class ChangeAllSplash(
        private val show: Boolean
    ) : EditSongTagState {
        override fun dispatch(
            allAdapter: EditSongTagListenerAdapter,
            ownedAdapter: EditSongTagListenerAdapter,
            noAllTagsTextView: CustomTextView,
            noOwnedTagsTextView: CustomTextView
        ) {
            noAllTagsTextView.setText(
                if (show)
                    R.string.tags_do_not_exist
                else
                    R.string.empty_string
            )
        }
    }

    @Parcelize
    data class ChangeOwnedSplash(
        private val show: Boolean
    ) : EditSongTagState {
        override fun dispatch(
            allAdapter: EditSongTagListenerAdapter,
            ownedAdapter: EditSongTagListenerAdapter,
            noAllTagsTextView: CustomTextView,
            noOwnedTagsTextView: CustomTextView
        ) {
            noOwnedTagsTextView.setText(
                if (show)
                    R.string.no_tags_attached
                else
                    R.string.empty_string
            )
        }
    }

    @Parcelize
    data class Error(private val error: String) : EditSongTagState {
        override fun dispatch(
            allAdapter: EditSongTagListenerAdapter,
            ownedAdapter: EditSongTagListenerAdapter,
            noAllTagsTextView: CustomTextView,
            noOwnedTagsTextView: CustomTextView
        ) = Unit
    }

    @Parcelize
    object Empty : EditSongTagState {
        override fun dispatch(
            allAdapter: EditSongTagListenerAdapter,
            ownedAdapter: EditSongTagListenerAdapter,
            noAllTagsTextView: CustomTextView,
            noOwnedTagsTextView: CustomTextView
        ) = Unit
    }
}