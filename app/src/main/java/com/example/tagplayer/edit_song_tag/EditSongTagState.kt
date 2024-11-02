package com.example.tagplayer.edit_song_tag

import com.example.tagplayer.R
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.main.presentation.CustomTextView

interface EditSongTagState {
    fun dispatch(
        allAdapter: EditSongTagListenerAdapter,
        ownedAdapter: EditSongTagListenerAdapter,
        noAllTagsTextView: CustomTextView,
        noOwnedTagsTextView: CustomTextView
    )
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = viewModel.clear()

    class DragAndDrop(
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
    }

    class ChangeAllTagsSplashState(
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

    class ChangeOwnedTagsSplashState(
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

    class Error(private val msg: String) : EditSongTagState {
        override fun dispatch(
            allAdapter: EditSongTagListenerAdapter,
            ownedAdapter: EditSongTagListenerAdapter,
            noAllTagsTextView: CustomTextView,
            noOwnedTagsTextView: CustomTextView
        ) = Unit
    }

    object Empty : EditSongTagState {
        override fun dispatch(
            allAdapter: EditSongTagListenerAdapter,
            ownedAdapter: EditSongTagListenerAdapter,
            noAllTagsTextView: CustomTextView,
            noOwnedTagsTextView: CustomTextView
        ) = Unit
    }
}