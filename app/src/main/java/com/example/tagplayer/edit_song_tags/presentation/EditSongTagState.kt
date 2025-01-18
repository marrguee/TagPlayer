package com.example.tagplayer.edit_song_tags.presentation

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.example.tagplayer.R
import com.example.tagplayer.core.domain.HandleUiStateUpdates
import com.example.tagplayer.main.presentation.CustomTextView

interface EditSongTagState : Parcelable {
    fun dispatch(
        allAdapter: EditSongTagListenerAdapter,
        ownedAdapter: EditSongTagListenerAdapter,
        noAllTagsTextView: CustomTextView,
        noOwnedTagsTextView: CustomTextView
    )
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = viewModel.clear()

    @SuppressLint("ParcelCreator")
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

        override fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit

        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeTypedList(allTags)
            dest.writeTypedList(ownedTags)
        }

        companion object CREATOR : Parcelable.Creator<DragAndDrop> {
            override fun createFromParcel(parcel: Parcel): DragAndDrop {
                val allTags = parcel.createTypedArrayList(TagUi.CREATOR) ?: emptyList()
                val ownedTags = parcel.createTypedArrayList(TagUi.CREATOR) ?: emptyList()
                return DragAndDrop(allTags, ownedTags)
            }

            override fun newArray(size: Int): Array<DragAndDrop?> = arrayOfNulls(size)
        }
    }

    @SuppressLint("ParcelCreator")
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

        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeByte(if (show) 1 else 0)
        }

        companion object CREATOR : Parcelable.Creator<ChangeAllTagsSplashState> {
            override fun createFromParcel(parcel: Parcel): ChangeAllTagsSplashState {
                val show = parcel.readByte() != 0.toByte()
                return ChangeAllTagsSplashState(show)
            }

            override fun newArray(size: Int): Array<ChangeAllTagsSplashState?> = arrayOfNulls(size)
        }
    }

    @SuppressLint("ParcelCreator")
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

        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeByte(if (show) 1 else 0)
        }

        companion object CREATOR : Parcelable.Creator<ChangeOwnedTagsSplashState> {
            override fun createFromParcel(parcel: Parcel): ChangeOwnedTagsSplashState {
                val show = parcel.readByte() != 0.toByte()
                return ChangeOwnedTagsSplashState(show)
            }

            override fun newArray(size: Int): Array<ChangeOwnedTagsSplashState?> = arrayOfNulls(size)
        }
    }

    @SuppressLint("ParcelCreator")
    class Error(private val error: String) : EditSongTagState {
        override fun dispatch(
            allAdapter: EditSongTagListenerAdapter,
            ownedAdapter: EditSongTagListenerAdapter,
            noAllTagsTextView: CustomTextView,
            noOwnedTagsTextView: CustomTextView
        ) = Unit

        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeString(error)
        }

        companion object CREATOR : Parcelable.Creator<Error> {
            override fun createFromParcel(parcel: Parcel): Error {
                val error = parcel.readString() ?: ""
                return Error(error)
            }

            override fun newArray(size: Int): Array<Error?> = arrayOfNulls(size)
        }
    }

    @SuppressLint("ParcelCreator")
    object Empty : EditSongTagState {
        override fun dispatch(
            allAdapter: EditSongTagListenerAdapter,
            ownedAdapter: EditSongTagListenerAdapter,
            noAllTagsTextView: CustomTextView,
            noOwnedTagsTextView: CustomTextView
        ) = Unit

        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int) = Unit

        @JvmField
        val CREATOR: Parcelable.Creator<Empty> = object : Parcelable.Creator<Empty> {
            override fun createFromParcel(parcel: Parcel): Empty = Empty
            override fun newArray(size: Int): Array<Empty?> = arrayOfNulls(size)
        }
    }
}