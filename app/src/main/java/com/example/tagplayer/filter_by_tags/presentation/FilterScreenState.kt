package com.example.tagplayer.filter_by_tags.presentation

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.example.tagplayer.core.domain.HandleUiStateUpdates

interface FilterScreenState : Parcelable {
    fun dispatch(adapter: FilterAdapter)
    fun consumed(viewModel: HandleUiStateUpdates.ClearObservable) = Unit

    @SuppressLint("ParcelCreator")
    class SelectedChangedScreen(
        private val list: List<FilterUi>
    ) : FilterScreenState {
        override fun dispatch(adapter: FilterAdapter) {
            adapter.submitList(list)
        }

        override fun describeContents(): Int = 0

        override fun writeToParcel(dest: Parcel, flags: Int) {
            dest.writeTypedList(list)
        }

        companion object CREATOR : Parcelable.Creator<SelectedChangedScreen> {
            override fun createFromParcel(parcel: Parcel): SelectedChangedScreen {
                val list = parcel.createTypedArrayList(FilterUi.CREATOR) ?: emptyList()
                return SelectedChangedScreen(list)
            }

            override fun newArray(size: Int): Array<SelectedChangedScreen?> = arrayOfNulls(size)
        }
    }

    @SuppressLint("ParcelCreator")
    object Empty : FilterScreenState {
        override fun dispatch(adapter: FilterAdapter) = Unit

        override fun describeContents(): Int = 0
        override fun writeToParcel(dest: Parcel, flags: Int) = Unit

        @JvmField
        val CREATOR: Parcelable.Creator<Empty> = object : Parcelable.Creator<Empty> {
            override fun createFromParcel(parcel: Parcel): Empty = Empty
            override fun newArray(size: Int): Array<Empty?> = arrayOfNulls(size)
        }
    }
}